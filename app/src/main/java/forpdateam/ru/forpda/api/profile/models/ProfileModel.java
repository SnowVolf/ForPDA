package forpdateam.ru.forpda.api.profile.models;

import android.text.Spanned;
import android.util.Pair;

import java.util.ArrayList;

import forpdateam.ru.forpda.api.profile.interfaces.IProfileModel;

/**
 * Created by radiationx on 03.08.16.
 */
public class ProfileModel implements IProfileModel {
    private String avatar, nick, status, group, regDate, alerts, onlineDate, gender, birthday, userTime, note, city;
    private Spanned sign, about;
    private ArrayList<Pair<String, String>> contacts = new ArrayList<>();
    private ArrayList<Pair<String, String>> devices = new ArrayList<>();
    private Pair<String, String> karma, sitePosts, comments, reputation, topics, posts;

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String arg) {
        avatar = arg;
    }

    @Override
    public String getNick() {
        return nick;
    }

    @Override
    public void setNick(String arg) {
        nick = arg;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String arg) {
        status = arg;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public void setGroup(String arg) {
        group = arg;
    }

    @Override
    public String getRegDate() {
        return regDate;
    }

    @Override
    public void setRegDate(String arg) {
        regDate = arg;
    }

    @Override
    public String getAlerts() {
        return alerts;
    }

    @Override
    public void setAlerts(String arg) {
        alerts = arg;
    }

    @Override
    public String getOnlineDate() {
        return onlineDate;
    }

    @Override
    public void setOnlineDate(String arg) {
        onlineDate = arg;
    }

    @Override
    public Spanned getSign() {
        return sign;
    }

    @Override
    public void setSign(Spanned arg) {
        sign = arg;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String arg) {
        gender = arg;
    }

    @Override
    public String getBirthDay() {
        return birthday;
    }

    @Override
    public void setBirthDay(String arg) {
        birthday = arg;
    }

    @Override
    public String getUserTime() {
        return userTime;
    }

    @Override
    public void setUserTime(String arg) {
        userTime = arg;
    }

    @Override
    public ArrayList<Pair<String, String>> getContacts() {
        return contacts;
    }

    @Override
    public ArrayList<Pair<String, String>> getDevices() {
        return devices;
    }

    @Override
    public Pair<String, String> getKarma() {
        return karma;
    }

    @Override
    public void setKarma(Pair<String, String> arg) {
        karma = arg;
    }

    @Override
    public Pair<String, String> getSitePosts() {
        return sitePosts;
    }

    @Override
    public void setSitePosts(Pair<String, String> arg) {
        sitePosts = arg;
    }

    @Override
    public Pair<String, String> getComments() {
        return comments;
    }

    @Override
    public void setComments(Pair<String, String> arg) {
        comments = arg;
    }

    @Override
    public Pair<String, String> getReputation() {
        return reputation;
    }

    @Override
    public void setReputation(Pair<String, String> arg) {
        reputation = arg;
    }

    @Override
    public Pair<String, String> getTopics() {
        return topics;
    }

    @Override
    public void setTopics(Pair<String, String> arg) {
        topics = arg;
    }

    @Override
    public Pair<String, String> getPosts() {
        return posts;
    }

    @Override
    public void setPosts(Pair<String, String> arg) {
        posts = arg;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String arg) {
        note = arg;
    }

    @Override
    public Spanned getAbout() {
        return about;
    }

    @Override
    public void setAbout(Spanned arg) {
        about = arg;
    }

    @Override
    public void addContact(Pair<String, String> arg) {
        contacts.add(arg);
    }

    @Override
    public void addDevice(Pair<String, String> arg) {
        devices.add(arg);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
