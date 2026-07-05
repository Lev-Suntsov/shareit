import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final EntityManager em;
    private final ItemServiceImpl service;
    private final UserServiceImpl userService;

    ItemDto dto = new ItemDto();

    @Test
    void addItemTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Lev");
        userDto.setEmail("testUser@email.com");
        UserDto savedUser = userService.saveUser(userDto);

        dto.setAvailable(true);
        dto.setDescription("TestItem");
        dto.setName("Item");
        dto.setUserId(1L);
        ItemDto saved = service.addNewItem(savedUser.getId(), dto);

        assertThat(saved.getDescription(), equalTo(dto.getDescription()));
        assertThat(saved.getName(), equalTo(dto.getName()));
    }

    @Test
    void deleteItemTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Lev");
        userDto.setEmail("testUseras@email.com");
        UserDto savedUser = userService.saveUser(userDto);


        dto.setAvailable(true);
        dto.setDescription("TestItem");
        dto.setName("Item");
        dto.setUserId(1L);
        ItemDto saved = service.addNewItem(savedUser.getId(), dto);

        Item beforeDelete = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class).setParameter("id", saved.getId())
                .getSingleResult();
        assertThat(beforeDelete, notNullValue());
        service.deleteItem(saved.getId());
        List<Item> result = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class)
                .setParameter("id", saved.getId())
                .getResultList();

        assertThat(result.isEmpty(), equalTo(true));
    }

    @Test
    void updateItem() {
        UserDto userDto = new UserDto();
        userDto.setName("Lev");
        userDto.setEmail("testUser@email.com");
        UserDto savedUser = userService.saveUser(userDto);

        dto.setAvailable(true);
        dto.setDescription("TestItem");
        dto.setName("Item");
        dto.setUserId(savedUser.getId());
        ItemDto saved = service.addNewItem(savedUser.getId(), dto);
        dto.setName("ItemUpdate");
        ItemDto updated = service.updateItem(savedUser.getId(), saved.getId(), dto);

        assertThat(updated.getName(), equalTo(dto.getName()));
    }

    @Test
    void findByIdTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Lev");
        userDto.setEmail("testUser@email.com");
        UserDto savedUser = userService.saveUser(userDto);

        dto.setAvailable(true);
        dto.setDescription("TestItem");
        dto.setName("Item");
        dto.setUserId(savedUser.getId());
        ItemDto saved = service.addNewItem(savedUser.getId(), dto);
        dto.setName("ItemUpdate");

        ItemDto findet = service.getItem(saved.getUserId(), saved.getId());

        assertThat(findet.getName(), equalTo(saved.getName()));
        assertThat(findet.getId(), equalTo(saved.getId()));
    }

    @Test
    void searchItemsTest() {
        UserDto userDto = new UserDto();
        userDto.setName("Lev");
        userDto.setEmail("searchUser@email.com");
        UserDto savedUser = userService.saveUser(userDto);

        ItemDto item1 = new ItemDto();
        item1.setAvailable(true);
        item1.setDescription("First search item");
        item1.setName("Drill");
        item1.setUserId(savedUser.getId());
        service.addNewItem(savedUser.getId(), item1);

        ItemDto item2 = new ItemDto();
        item2.setAvailable(true);
        item2.setDescription("Second item");
        item2.setName("Hammer");
        item2.setUserId(savedUser.getId());
        service.addNewItem(savedUser.getId(), item2);

        List<ItemDto> result = service.search("Drill");

        assertThat(result.size(), equalTo(1));
        assertThat(result.get(0).getName(), equalTo("Drill"));
    }
}
