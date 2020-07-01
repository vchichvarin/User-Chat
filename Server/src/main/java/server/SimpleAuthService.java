package server;

import java.util.ArrayList;
import java.util.List;

public class SimpleAuthService implements AuthService {

    private class UserData{
        String login;
        String password;
        String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<UserData> users;

    public SimpleAuthService() {
        this.users = new ArrayList<>();

        for (int i = 1; i <= 10 ; i++) {
            users.add(new UserData("login"+i, "pass"+i, "nick"+i));
        }

        for (int i = 1; i <= 3 ; i++) {
            users.add(new UserData(""+i, ""+i, "simple_nick"+i));
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData o:users ) {
            if(o.login.equals(login) && o.password.equals(password)){
                return o.nickname;
            }
        }

        return null;
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        for (UserData o:users ) {
            if(o.login.equals(login)) {
                return false;
            }
        }
        users.add(new UserData(login, password, nickname));
        return true;
    }

    @Override
    public boolean changeNick(String oldNickname, String newNickname) {
        return false;
    }
}
