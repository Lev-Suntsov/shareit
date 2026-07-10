import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.request.RequestDto;
import ru.practicum.shareit.request.RequestDtoForGet;
import ru.practicum.shareit.request.RequestServiceImpl;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImplTest {
    private final RequestServiceImpl service;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    ItemDto itemDto = new ItemDto();
    UserDto saved = new UserDto();
    RequestDto request = new RequestDto();


    @Test
    void addRequestTest(){
        request.setItemId(1L);
        request.setCreated(LocalDateTime.now());
        request.setDescription("testRequest");
        request.setUserId(1L);

        RequestDto saved = service.add(request.getUserId(), request);

        assertThat(saved.getId(), notNullValue());
        assertThat(saved.getItemId(), equalTo(request.getItemId()));
        assertThat(saved.getDescription(), equalTo(request.getDescription()));
        assertThat(saved.getCreated(), equalTo(request.getCreated()));
        assertThat(saved.getUserId(), equalTo(request.getUserId()));
    }

    @Test
    void getTest(){
        UserDto dto = new UserDto();
        dto.setName("Lev");
        dto.setEmail("testUser@email.com");

         saved = userService.saveUser(dto);

        itemDto.setAvailable(true);
        itemDto.setDescription("TestItem");
        itemDto.setName("Item");
        itemDto.setUserId(1L);
        ItemDto sevedItem = itemService.addNewItem(saved.getId(), itemDto);

        RequestDto request = new RequestDto();
        request.setItemId(sevedItem.getId());
        request.setCreated(LocalDateTime.now());
        request.setDescription("testRequest");
        request.setUserId(saved.getId());
        service.add(request.getUserId(), request);

        RequestDto request2 = new RequestDto();
        request2.setItemId(sevedItem.getId());
        request2.setCreated(LocalDateTime.now());
        request2.setDescription("testRequest2");
        request2.setUserId(saved.getId());
        service.add(request2.getUserId(), request2);

        RequestDto request3 = new RequestDto();
        request3.setItemId(sevedItem.getId());
        request3.setCreated(LocalDateTime.now());
        request3.setDescription("testRequest3");
        request3.setUserId(saved.getId());
        service.add(request3.getUserId(), request3);

        List<RequestDtoForGet> requestDtoForGets = service.get(saved.getId());

        for(RequestDtoForGet o : requestDtoForGets){
            assertThat(o.getUserId(), equalTo(saved.getId()));
        }
        itemDto = sevedItem;
    }

    @Test
    void getUserUserRequestTest(){
        for(UserDto u: userService.getAllUsers()){
            if(u.getEmail().equals("leviksun@gmail.com")){

                RequestDto request = new RequestDto();
                request.setItemId(itemDto.getId());
                request.setCreated(LocalDateTime.now());
                request.setDescription("testRequest");
                request.setUserId(u.getId());
                service.add(request.getUserId(), request);
                List<RequestDtoForGet> requestDtoForGets = service.getOtherUsersRequests(u.getId());
                for(RequestDtoForGet o : requestDtoForGets){
                    assertThat(o.getUserId(), equalTo(saved.getId()));
                }
            }
        }
    }

    @Test
    void getRequestByIdTest(){
        UserDto dto = new UserDto();
        dto.setName("Lev");
        dto.setEmail("testUser@email.com");

        saved = userService.saveUser(dto);

        itemDto.setAvailable(true);
        itemDto.setDescription("TestItem");
        itemDto.setName("Item");
        itemDto.setUserId(1L);
        ItemDto sevedItem = itemService.addNewItem(saved.getId(), itemDto);

        RequestDto request = new RequestDto();
        request.setItemId(sevedItem.getId());
        request.setCreated(LocalDateTime.now());
        request.setDescription("testRequest");
        request.setUserId(saved.getId());
        request = service.add(request.getUserId(), request);

        RequestDtoForGet unswer = service.getById(request.getId());

        assertThat(unswer.getUserId(), equalTo(request.getUserId()));
        assertThat(unswer.getDescription(), equalTo(request.getDescription()));
    }
}
