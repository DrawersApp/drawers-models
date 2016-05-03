package dao;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by nishant.pathak on 03/05/16.
 */
public class ChatConstant {
    public enum ChatType {
        FILE(0),
        IMAGE(1),
        VIDEO(2),
        TEXT(3),
        CONTACT(4),
        MAP(5),
        NOTIFICATION(6),
        CALL(7),
        QA(8),
        QAR(9);

        int pos;
        ChatType(int p) {
            pos = p;
        }

        public boolean isMediaMessage() {
            return pos <= 2;
        }

        public boolean isTextMessage() {
            return pos == 3;
        }
    }


    static HashSet<ChatType> mType = new HashSet<>(Arrays.asList(ChatType.values()));

    static public boolean validType(ChatType type) {
        return mType.contains(type);
    }
}
