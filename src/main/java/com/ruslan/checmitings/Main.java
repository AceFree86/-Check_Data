package com.ruslan.checmitings;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        CheckData checkData = new CheckData();
        checkData.get();
        checkData.sentMeetings();
    }
}