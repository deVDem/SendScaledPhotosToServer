package ru.devdem.sendscaledphotososerver.helpers.objects;


public class User {

    // мне пофиг, что тут много инфы
    private int mId;
    private String mName;
    private String mEmail;
    private String mLogin;
    private String mUrlImage;
    private int mGroup;
    private String mPassword;
    private String mToken;
    private boolean mPro;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getUrlImage() {
        return mUrlImage;
    }

    public void setUrlImage(String urlImage) {
        mUrlImage = urlImage;
    }

    public int getGroup() {
        return mGroup;
    }

    public void setGroup(int group) {
        mGroup = group;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public boolean isPro() {
        return mPro;
    }

    public void setPro(boolean pro) {
        mPro = pro;
    }
}
