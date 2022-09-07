package leetcode.AZInterview;

import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class CalendarEvent {

    Date date;
    int start;
    int end;

}

public class Interview {

    public int calcDuration(List<CalendarEvent> events) {

        if (events.size() == 0) return 0;

//        int mark;

        int val= events.get(events.size()-1).end - events.get(0).start;
        int dif=0;

        for (int i = 1; i < events.size() - 1; i++) {

            CalendarEvent cur = events.get(i);

            CalendarEvent prev = events.get(i-1);

        if(cur.start-prev.end>0) dif+=cur.start-prev.end;




        }


        return val;

    }

}
