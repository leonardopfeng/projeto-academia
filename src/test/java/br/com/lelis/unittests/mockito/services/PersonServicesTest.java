package br.com.lelis.unittests.mockito.services;

import br.com.lelis.data.vo.PersonVO;
import br.com.lelis.exceptions.RequiredObjectIsNullException;
import br.com.lelis.model.Person;
import br.com.lelis.repositories.PersonRepository;
import br.com.lelis.services.PersonServices;
import br.com.lelis.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    // Inject a mock for the repository
    @InjectMocks
    private PersonServices service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    /*
    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var PersonOne = people.get(1);
        assertNotNull(PersonOne);
        assertNotNull(PersonOne.getKey());
        assertNotNull(PersonOne.getLinks());
        assertTrue(PersonOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", PersonOne.getFirstName());
        assertEquals("Last Name Test1", PersonOne.getLastName());
        assertEquals("Address Test1", PersonOne.getAddress());
        assertEquals("Female", PersonOne.getGender());

        var PersonFour = people.get(4);
        assertNotNull(PersonFour);
        assertNotNull(PersonFour.getKey());
        assertNotNull(PersonFour.getLinks());
        assertTrue(PersonFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("First Name Test4", PersonFour.getFirstName());
        assertEquals("Last Name Test4", PersonFour.getLastName());
        assertEquals("Address Test4", PersonFour.getAddress());
        assertEquals("Male", PersonFour.getGender());

        var PersonSeven = people.get(7);
        assertNotNull(PersonSeven);
        assertNotNull(PersonSeven.getKey());
        assertNotNull(PersonSeven.getLinks());
        assertTrue(PersonSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("First Name Test7", PersonSeven.getFirstName());
        assertEquals("Last Name Test7", PersonSeven.getLastName());
        assertEquals("Address Test7", PersonSeven.getAddress());
        assertEquals("Female", PersonSeven.getGender());
    }

    @Test
    void findById() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }
    */

    @Test
    void create() {
        // entity before calling the repo
        Person entity = input.mockEntity(1);
        // entity after calling the repo
        Person persisted  = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        // entity before calling the repo
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        // entity after calling the repo
        Person persisted  = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        // if the entity exists in the repo, it persists the update
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Address Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        String expectedMessage = "It is not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}