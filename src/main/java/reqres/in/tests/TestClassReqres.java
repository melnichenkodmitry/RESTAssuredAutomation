package reqres.in.tests;

import reqres.in.payloads.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reqres.in.payloads.*;

import java.time.Clock;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestClassReqres {
    private static final String URL = "https://reqres.in";
    private static final Integer ID = 4;
    private static final String TOKEN = "QpwL5tke4Pnpja7X4";

    /**
     * ТECT-КЕЙС №1 <br>
     * Метод GET. <br>
     * Получение списка пользователей.<br>
     * Проверка вхождения ID пользователя в строку Avatar, окончания email на @reqres.in, вхождения пользователей в ENUM
     */

    @Test
    @DisplayName("Получение списка пользователей")
    public void checkAvatarAndEmail() {
        List<Datum> data = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "/api/users?page=2")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .body()
                .jsonPath()
                .getList("data", Datum.class);
        //System.out.println(data);
        //Проверка, что в строке avatar содержится идентификатор пользователя. Первый ассерт
        data.forEach(x-> Assertions.assertTrue(x.getAvatar().contains(String.valueOf(x.getId()))));
        //Проверка того, что каждая строка email заканчивается на @reqres.in. Второй ассерт
        data.forEach(x -> Assertions.assertTrue(x.getEmail().endsWith("@reqres.in")));
        //Проверка того, входят ли распарсенные пользователи в ENUM. Третий ассерт
        List<Users> usersExpected = Arrays.stream(Users.values()).toList();
        List<String> usersActual = data.stream().map(Datum::getLast_name).toList();
        Assertions.assertTrue(usersExpected.toString().equalsIgnoreCase(usersActual.toString()));
    }

    /**
     * ТЕСТ-КЕЙС №2 <br>
     * Метод POST. <br>
     * Проверка успешной регистрации. <br>
     * Проверка, что ответ пришел не пустой, и токен и идентификатор совпадают.
     */

    @Test
    @DisplayName("Успешная регистрация")
    public void registerSuccessful(){
        ReqRegSuccessful user = new ReqRegSuccessful("eve.holt@reqres.in", "pistol");
        ResRegSuccessful successReg = given()
                .body(user)
                .when()
                .contentType(ContentType.JSON)
                .post(URL + "/api/register")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .as(ResRegSuccessful.class);
        //System.out.println(successReg);
        //Проверка, что ответ пришел не пустой. Первый ассерт
        Assertions.assertNotNull(successReg.getId());
        Assertions.assertNotNull(successReg.getToken());
        //Проверка, что токен и идентификатор совпадают. Второй ассерт
        Assertions.assertEquals(ID,successReg.getId());
        Assertions.assertEquals(TOKEN,successReg.getToken());
    }

    /**
     * ТЕСТ-КЕЙС №3 <br>
     * Метод POST. <br>
     * Проверка неуспешной регистрации. <br>
     * Проверка, что пришедший ответ совпадает с ожидаемым.
     */

    @Test
    @DisplayName("Неуспешная регистрация")
    public void registerUnsuccessful() {
        ReqRegSuccessful user = new ReqRegSuccessful("eve.holt@reqres.in");
        ResRegUnsuccessful unsuccessReg = given()
                .body(user)
                .when()
                .contentType(ContentType.JSON)
                .post(URL + "/api/register")
                .then()
                .statusCode(400)
                .log()
                .all()
                .extract()
                .as(ResRegUnsuccessful.class);
        System.out.println(unsuccessReg);
        //Проверка, что ожидаемый ответ совпадает с актуальным
        Assertions.assertEquals("Missing password", unsuccessReg.getError());
    }

    /**
     * ТЕСТ-КЕЙС №4 <br>
     * Метод PUT. <br>
     * Изменение пользователя
     * Проверка, что name и job равны в ожидаемом и актуальном результатах.
     * Сравнение системного времени с временем создания пользователя
     */

    @Test
    @DisplayName("Обновление пользователя")
    public void userUpdate() {
        ReqUserUpdate user = new ReqUserUpdate("morpheus", "zion resident");
        ResUserUpdate userUpdate = given()
                .body(user)
                .when()
                .contentType(ContentType.JSON)
                .put(URL + "/api/users/2")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .as(ResUserUpdate.class);
        //System.out.println(userUpdate);
        String currentTime = Clock.systemUTC().instant().toString().replaceAll("(.{14})$", "");
        //Проверка совпадения name в ожидаемом и актуальном результатах. Первый ассерт
        Assertions.assertEquals(user.getName(), userUpdate.getName());
        //System.out.println(user.getName() + " | " + userUpdate.getName());
        //Проверка совпадения job в ожидаемом и актуальном результатах. Второй ассерт
        Assertions.assertEquals(user.getJob(), userUpdate.getJob());
        //System.out.println(user.getJob() + " | " + userUpdate.getJob());
        //Сравнение системного времени с временем создания пользователя. Третий ассерт
        Assertions.assertEquals(currentTime,userUpdate.getUpdatedAt().replaceAll("(.{8})$", ""));
        //System.out.println(currentTime + " | " + userUpdate.getUpdatedAt().replaceAll("(.{8})$", ""));
    }

    /**
     * ТЕСТ-КЕЙС №5
     * Метод DELETE <br>
     * Удаление пользователя
     */

    @Test
    @DisplayName("Удаление пользователя")
    public void deleteUser() {
        given()
                .when()
                .delete(URL + "/api/users/2")
                .then()
                .statusCode(204)
                .log()
                .all();
    }
}
