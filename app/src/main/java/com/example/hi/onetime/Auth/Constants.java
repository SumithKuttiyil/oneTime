package com.example.hi.onetime.Auth;

/**
 * Created by Hi on 16-06-2017.
 */

import java.util.Random;

public class Constants {

    public static final String IDENTITY_POOL_ID = "us-east-1:d9a9acbd-cf87-4125-899f-e7d24d33dc03";
    // Note that spaces are not allowed in the table name
    public static final String TEST_TABLE_NAME = "amazonapp-mobilehub-191482968-Locations";

    public static final Random random = new Random();
    public static final String[] NAMES = new String[] {
            "Norm", "Jim", "Jason", "Zach", "Matt", "Glenn", "Will", "Wade", "Trevor", "Jeremy",
            "Ryan", "Matty", "Steve", "Pavel"
    };

    public static String getRandomName() {
        int name = random.nextInt(NAMES.length);

        return NAMES[name];
    }

    public static int getRandomScore() {
        return random.nextInt(1000) + 1;
    }
}