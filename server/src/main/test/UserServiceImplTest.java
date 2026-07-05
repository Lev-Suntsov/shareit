import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final EntityManager em;
    private final UserService service;
    UserDto dto = new UserDto();

    @Test
    void saveUserTest(){
        dto.setName("Lev");
        dto.setEmail("testUser@email.com");

        service.saveUser(dto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);

        User user = query.setParameter("email", dto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(dto.getName()));
        assertThat(user.getEmail(), equalTo(dto.getEmail()));
    }

    @Test
    void getUserTest() {
        // подготовка данных
        dto = new UserDto();
        dto.setName("Levф");
        dto.setEmail("leviksun.s@email.com");

        dto = service.saveUser(dto);

        UserDto found = service.findUserById(dto.getId());

        assertThat(found.getId(), notNullValue());
        assertThat(found.getId(), equalTo(dto.getId()));
        assertThat(found.getName(), equalTo(dto.getName()));
        assertThat(found.getEmail(), equalTo(dto.getEmail()));
    }

    @Test
    void updateUserTest(){
        UserDto old = service.findUserById(dto.getId());

        dto.setEmail("leviksun@gmail.com");
        dto.setName("levSuntsov");

        UserDto update = service.updateUser(old.getId(), dto);
        assertThat(update.getId(), notNullValue());
        assertThat(update.getId(), equalTo(old.getId()));
        assertThat(update.getName(), equalTo(dto.getName()));
        assertThat(update.getEmail(), equalTo(dto.getEmail()));
    }

    @Test
    void deleteUserTest(){
            UserDto dto = new UserDto();
            dto.setName("Lev");
            dto.setEmail("testUser@email.com");

            UserDto saved = service.saveUser(dto);
            Long userId = saved.getId();

            User beforeDelete = em.createQuery(
                            "SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", userId)
                    .getSingleResult();
            assertThat(beforeDelete, notNullValue());
            service.deleteUser(userId);
            List<User> result = em.createQuery(
                            "SELECT u FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", userId)
                    .getResultList();

            assertThat(result.isEmpty(), equalTo(true));
    }
}
