package me.arblitroshani.androidtest.extra;

import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.model.AppointmentCalendarEvent;
import me.arblitroshani.androidtest.model.HomeSection;

public final class Constants {

    public static final class Appointments {

        public static final String STATUS_COMPLETED = "completed";
        public static final String STATUS_PENDING = "pending";
        public static final String STATUS_CONFIRMED = "confirmed";

        public static final String SERVICE_ORTHODONTICS = "orthodontics";
        public static final String SERVICE_PERIODONTICS = "peiodontics";

        public static int RESULT_OK = 5;

        public static List<CalendarEvent> getMockList() {
            List<CalendarEvent> events = new ArrayList<>();

            Calendar startTime1 = Calendar.getInstance();
            Calendar endTime1 = Calendar.getInstance();
            startTime1.set(Calendar.HOUR_OF_DAY, 10);
            startTime1.set(Calendar.MINUTE, 0);
            endTime1.set(Calendar.HOUR_OF_DAY, 11);
            endTime1.set(Calendar.MINUTE, 0);

            AppointmentCalendarEvent event1 = new AppointmentCalendarEvent("Visit of Harpa",
                    "description", startTime1, endTime1, Appointments.STATUS_COMPLETED,
                    Appointments.SERVICE_ORTHODONTICS, true);
            events.add(event1);

            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 14);
            startTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.HOUR_OF_DAY, 15);
            endTime.set(Calendar.MINUTE, 0);

            AppointmentCalendarEvent event = new AppointmentCalendarEvent("Visit of Harpa",
                    "description", startTime, endTime, Appointments.STATUS_PENDING,
                    Appointments.SERVICE_ORTHODONTICS, true);
            events.add(event);

            Calendar startTime2 = Calendar.getInstance();
            Calendar endTime2 = Calendar.getInstance();
            startTime2.set(Calendar.HOUR_OF_DAY, 18);
            startTime2.set(Calendar.MINUTE, 30);
            endTime2.set(Calendar.HOUR_OF_DAY, 19);
            endTime2.set(Calendar.MINUTE, 0);

            AppointmentCalendarEvent event2 = new AppointmentCalendarEvent("Visit of Harpa",
                    "description", startTime2, endTime2, Appointments.STATUS_CONFIRMED,
                    Appointments.SERVICE_ORTHODONTICS, true);
            events.add(event2);

            Collections.sort(events, (a, b) -> a.getStartTime().compareTo(b.getStartTime()));
            return events;
        }
    }

    public static final class HomeSections {

        private static List<HomeSection> getCommonSections() {
            List<HomeSection> commonSections = new ArrayList<>();

            commonSections.add(new HomeSection(
                    "Profile",
                    "this is a subtitle",
                    "Profile",
                    R.color.teal_blue,
                    R.drawable.ic_account_circle_white_48dp,
                    true)
            );

            return commonSections;
        }

        private static List<HomeSection> getClinicSections() {
            List<HomeSection> commonSections = new ArrayList<>();

            commonSections.add(new HomeSection(
                    "Our services",
                    "this is a subtitle",
                    "Services",
                    R.color.saffron,
                    R.drawable.ic_dashboard_white_48dp,
                    false)
            );
            commonSections.add(new HomeSection(
                    "Clinic info",
                    "this is a subtitle",
                    "Home",
                    R.color.giants_orange_light,
                    R.drawable.ic_info_outline_white_48dp,
                    false)
            );

            return commonSections;
        }

        public static List<HomeSection> getUserSections() {
            List<HomeSection> userSections = new ArrayList<>();

            for (HomeSection section: getCommonSections()){
                userSections.add(section);
            }

            userSections.add(new HomeSection(
                    "Appointments",
                    "this is a subtitle",
                    "Activity:Appointments",
                    R.color.dark_slate_gray,
                    R.drawable.ic_today_white_48dp,
                    true)
            );
            userSections.add(new HomeSection(
                    "Treatments",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp,
                    true)
            );

            for (HomeSection section: getClinicSections()) {
                userSections.add(section);
            }

            return userSections;
        }

        public static List<HomeSection> getDoctorSections() {
            List<HomeSection> doctorSections = new ArrayList<>();

            for (HomeSection section: getCommonSections()){
                doctorSections.add(section);
            }

            doctorSections.add(new HomeSection(
                    "Appointments",
                    "this is a subtitle",
                    "Home",
                    R.color.dark_slate_gray,
                    R.drawable.ic_today_white_48dp,
                    true)
            );
            doctorSections.add(new HomeSection(
                    "Availability",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp,
                    true)
            );
            doctorSections.add(new HomeSection(
                    "Patients",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp,
                    true)
            );
            doctorSections.add(new HomeSection(
                    "Payment",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp,
                    true)
            );

            for (HomeSection section: getClinicSections()) {
                doctorSections.add(section);
            }

            return doctorSections;
        }
    }
}
