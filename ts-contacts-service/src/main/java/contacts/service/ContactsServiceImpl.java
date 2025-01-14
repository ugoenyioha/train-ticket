package contacts.service;

import contacts.entity.*;
import edu.fudan.common.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import contacts.repository.ContactsRepository;

import java.util.ArrayList;

@Service
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsRepository contactsRepository;

    String success = "Success";
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactsServiceImpl.class);

    @Override
    public Response<Contacts> findContactsById(String id, HttpHeaders headers) {
        LOGGER.info("FIND CONTACTS BY ID: " + id);
        Contacts contacts = contactsRepository.findById(id).orElse(null);
        if (contacts != null) {
            LOGGER.info("[findContactsById][Find contacts]");
            return new Response<>(1, success, contacts);
        } else {
            LOGGER.error("[findContactsById][No contacts found][contactsId: {}]", id);
            return new Response<>(0, "No contacts found", null);
        }
    }

    @Override
    public Response<ArrayList<Contacts>> findContactsByAccountId(String accountId, HttpHeaders headers) {
        ArrayList<Contacts> arr = contactsRepository.findByAccountId(accountId);
        LOGGER.info("[findContactsByAccountId][Query Result Size: {}]", arr.size());
        return new Response<>(1, success, arr);
    }

    @Override
    public Response<Contacts> createContacts(Contacts contacts, HttpHeaders headers) {
        Contacts contactsTemp = contactsRepository.findByAccountIdAndDocumentTypeAndDocumentType(
            contacts.getAccountId(), contacts.getDocumentNumber(), contacts.getDocumentType());
        if (contactsTemp != null) {
            LOGGER.warn("[createContacts][Already Exists][Id: {}]", contacts.getId());
            return new Response<>(0, "Already Exists", contactsTemp);
        } else {
            Contacts newContact = contactsRepository.save(contacts);
            return new Response<>(1, "Create Success", newContact);
        }
    }

    @Override
    public Response<Contacts> create(Contacts addContacts, HttpHeaders headers) {
        Contacts c = contactsRepository.findByAccountIdAndDocumentTypeAndDocumentType(
            addContacts.getAccountId(), addContacts.getDocumentNumber(), addContacts.getDocumentType());
        if (c != null) {
            LOGGER.warn("[create][Contacts exists][contactId: {}]", addContacts.getId());
            return new Response<>(0, "Contacts already exists", null);
        } else {
            Contacts contacts = contactsRepository.save(addContacts);
            LOGGER.info("[create][Success]");
            return new Response<>(1, "Create success", contacts);
        }
    }

    @Override
    public Response<String> delete(String contactsId, HttpHeaders headers) {
        contactsRepository.deleteById(contactsId);
        Contacts contacts = contactsRepository.findById(contactsId).orElse(null);
        if (contacts == null) {
            LOGGER.info("[delete][Success]");
            return new Response<>(1, "Delete success", contactsId);
        } else {
            LOGGER.error("[delete][Failed][contactsId: {}]", contactsId);
            return new Response<>(0, "Delete failed", contactsId);
        }
    }

    @Override
    public Response<Contacts> modify(Contacts contacts, HttpHeaders headers) {
        Response<Contacts> oldContactResponse = findContactsById(contacts.getId(), null);
        Contacts oldContacts = oldContactResponse.getData();
        if (oldContacts == null) {
            LOGGER.error("[modify][Not found][contactId: {}]", contacts.getId());
            return new Response<>(0, "Contacts not found", null);
        } else {
            oldContacts.setName(contacts.getName());
            oldContacts.setDocumentType(contacts.getDocumentType());
            oldContacts.setDocumentNumber(contacts.getDocumentNumber());
            oldContacts.setPhoneNumber(contacts.getPhoneNumber());
            contactsRepository.save(oldContacts);
            LOGGER.info("[modify][Success]");
            return new Response<>(1, "Modify success", oldContacts);
        }
    }

    @Override
    public Response<ArrayList<Contacts>> getAllContacts(HttpHeaders headers) {
        ArrayList<Contacts> contacts = contactsRepository.findAll();
        if (contacts != null && !contacts.isEmpty()) {
            return new Response<>(1, success, contacts);
        } else {
            LOGGER.error("[getAllContacts][No content]");
            return new Response<>(0, "No content", null);
        }
    }
}


