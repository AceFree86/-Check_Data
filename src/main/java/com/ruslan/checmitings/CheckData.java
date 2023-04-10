package com.ruslan.checmitings;

import com.ruslan.checmitings.db.DBHelper;
import com.ruslan.checmitings.db.DataModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CheckData {

    private final DBHelper db = new DBHelper();
    private static final Bot bot = new Bot();

    public void get() {

        final String name_pr = "data_pr.json";
        final String name_zka = "data_zka.json";
        final String name_ug = "data_ug.json";
        final String name_vb = "data_vb.json";

        for (String list : new String[]{name_pr, name_zka, name_ug, name_vb}) {
            getData(list);
        }
    }

    private JSONArray readJson(String file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(new FileReader(file, StandardCharsets.UTF_8));
    }

    private void getData(String file) {
        ArrayList<DataModel> inputList = db.getUserInput();
        if (!inputList.isEmpty()) {
            for (DataModel input : inputList) {
                try {
                    JSONArray jsons = readJson(file);
                    for (Object list : jsons) {
                        JSONObject jsonObject = (JSONObject) list;
                        if (jsonObject.get("involved").toString().toLowerCase().replace("'", " ").replace("\"", " ")
                                .contains(input.text().toLowerCase().replace("'", " ").replace("\"", " "))
                                || jsonObject.get("number").toString().contains(input.text())) {
                            String date = (String) jsonObject.get("date");
                            String number = (String) jsonObject.get("number");
                            String judge = (String) jsonObject.get("judge");
                            String involved = (String) jsonObject.get("involved");
                            String description = (String) jsonObject.get("description");
                            String forma = (String) jsonObject.get("forma");
                            String text = date + "</b>" + number + "</b>" + judge + "</b>" + involved + "</b>" + description + "</b>" + forma;
                            if (!db.checkRow(text)) {
                                db.insertResult(new DataModel(-1, input.userId(), text, 1));
                            } else {
                                System.out.println("no");
                            }
                        }
                    }
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sentMeetings() throws IOException {
        final String CHAT_ID = "552974553";
        ArrayList<DataModel> meetings = db.getMeetings(1);
        if (!meetings.isEmpty()) {
            for (DataModel meeting : meetings) {
                try {
                    Thread.sleep(30000);
                    String[] str = meeting.text().split("</b>");
                    String text = "Доброго дня, було знайдено засідання!%0A<a href='http://www.example.com'>Дата</a> : <b>" + str[0] + "</b> год.%0A" +
                            "<a href='http://www.example.com'>№ Справи</a> : " + str[1] + "%0A" +
                            "<a href='http://www.example.com'>Суддя</a> : " + str[2] + "%0A__________%0A" +
                            "<a href='http://www.example.com'>Сторони по справі</a> : <b>" + str[3] + ".</b>%0A" +
                            "<a href='http://www.example.com'>Суть</a> : <b>" + str[4] + ".</b>%0A" +
                            "<a href='http://www.example.com'>" + str[5] + "</a>.%0A__________";
                    bot.setMsg(Integer.toString(meeting.userId()), text);
                    db.updateMeetings(meeting.id(), 1, 2);
                } catch (Exception e) {
                    bot.setMsg(CHAT_ID, "🤚Не відправлено🤬.");
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
