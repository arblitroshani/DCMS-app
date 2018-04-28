package me.arblitroshani.androidtest.extra;

import java.util.ArrayList;
import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.model.HomeSection;

public final class Constants {

    public static final class HomeSections {

        private static List<HomeSection> getCommonSections() {
            List<HomeSection> commonSections = new ArrayList<>();

            commonSections.add(new HomeSection(
                    "Profile",
                    "this is a subtitle",
                    "Home",
                    R.color.teal_blue,
                    R.drawable.ic_account_circle_white_48dp)
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
                    R.drawable.ic_dashboard_white_48dp)
            );
            commonSections.add(new HomeSection(
                    "Clinic info",
                    "this is a subtitle",
                    "Home",
                    R.color.giants_orange_light,
                    R.drawable.ic_info_outline_white_48dp)
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
                    "Home",
                    R.color.dark_slate_gray,
                    R.drawable.ic_today_white_48dp)
            );
            userSections.add(new HomeSection(
                    "Treatments",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp)
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
                    R.drawable.ic_today_white_48dp)
            );
            doctorSections.add(new HomeSection(
                    "Availability",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp)
            );
            doctorSections.add(new HomeSection(
                    "Patients",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp)
            );
            doctorSections.add(new HomeSection(
                    "Payment",
                    "this is a subtitle",
                    "Home",
                    R.color.gunmetal,
                    R.drawable.ic_receipt_white_48dp)
            );

            for (HomeSection section: getClinicSections()) {
                doctorSections.add(section);
            }

            return doctorSections;
        }
    }
}
