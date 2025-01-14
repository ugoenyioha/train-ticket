package contacts.init;

import contacts.entity.Contacts;
import contacts.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private ContactsService contactsService;

    @Override
    public void run(String... args) {
        Contacts contactsOne = new Contacts();
        contactsOne.setAccountId("4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f");
        contactsOne.setDocumentType(1);
        contactsOne.setName("Contacts_One");
        contactsOne.setDocumentNumber("DocumentNumber_One");
        contactsOne.setPhoneNumber("ContactsPhoneNumber_One");
        contactsOne.setId("CONTACTS_ONE_ID");
        contactsService.create(contactsOne, null);

        Contacts contactsTwo = new Contacts();
        contactsTwo.setAccountId("4d2a46c7-71cb-4cf1-b5bb-b68406d9da6f");
        contactsTwo.setDocumentType(1);
        contactsTwo.setName("Contacts_Two");
        contactsTwo.setDocumentNumber("DocumentNumber_Two");
        contactsTwo.setPhoneNumber("ContactsPhoneNumber_Two");
        contactsTwo.setId("CONTACTS_TWO_ID");
        contactsService.create(contactsTwo, null);
    }
}
