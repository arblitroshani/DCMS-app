package me.arblitroshani.androidtest.extra;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.model.HomeSection;

public final class Constants {

    public static final class Appointments {

        public static final String STATUS_COMPLETED = "completed";
        public static final String STATUS_PENDING = "pending";
        public static final String STATUS_CONFIRMED = "confirmed";

        public static final String SERVICE_ORTHODONTICS = "Orthodontics";
        public static final String SERVICE_PERIODONTICS = "Periodontics";

        //public static final String DATE_FORMAT = "dd/MM/yyyy";
        public static final String DATE_FORMAT = "MMM d, yyyy";
        public static final String TIME_FORMAT = "KK:mm a";

        public static int RESULT_OK = 5;
    }

    public static final class Treatments {
        public static final String DATE_FORMAT = Appointments.DATE_FORMAT;
    }

    public static final class HomeSections {

        private static List<HomeSection> getCommonSections() {
            List<HomeSection> commonSections = new ArrayList<>();

            commonSections.add(new HomeSection(
                    "Profile",
                    "this is a subtitle",
                    "Profile",
                    R.color.teal_blue,
                    R.drawable.outline_account_circle_white_48,
                    true)
            );

            return commonSections;
        }

        private static List<HomeSection> getClinicSections() {
            List<HomeSection> commonSections = new ArrayList<>();

            commonSections.add(new HomeSection(
                    "Services",
                    "this is a subtitle",
                    "Services",
                    R.color.saffron,
                    R.drawable.outline_dashboard_white_48,
                    false)
            );
            commonSections.add(new HomeSection(
                    "Clinic info",
                    "this is a subtitle",
                    "Home",
                    R.color.giants_orange_light,
                    R.drawable.outline_info_white_48,
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
                    R.drawable.outline_today_white_48,
                    true)
            );
            userSections.add(new HomeSection(
                    "Treatments",
                    "this is a subtitle",
                    "Treatments",
                    R.color.gunmetal,
                    R.drawable.outline_receipt_white_48,
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
