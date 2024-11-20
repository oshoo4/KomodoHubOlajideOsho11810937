package com.olajideosho.komodohub;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.olajideosho.komodohub.data.model.Classroom;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import com.olajideosho.komodohub.data.model.Message;
import com.olajideosho.komodohub.data.model.Species;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.data.repository.ConservationActivityRepository;
import com.olajideosho.komodohub.data.repository.MessageRepository;
import com.olajideosho.komodohub.data.repository.SpeciesRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;

public class KomodoHubApplication extends Application {

    SpeciesRepository speciesRepository;
    UserRepository userRepository;
    ClassroomRepository classroomRepository;
    MessageRepository messageRepository;
    ConservationActivityRepository conservationActivityRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        speciesRepository = new SpeciesRepository(this);
        userRepository = new UserRepository(this);
        classroomRepository = new ClassroomRepository(this);
        messageRepository = new MessageRepository(this);
        conservationActivityRepository = new ConservationActivityRepository(this);
        if (
                speciesRepository.getAllSpecies().isEmpty()
                && userRepository.getAllUsers().isEmpty()
                && classroomRepository.getAllClassrooms().isEmpty()
                && messageRepository.getAllMessages().isEmpty()
                && conservationActivityRepository.getAllConservationActivities().isEmpty()
        ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    seedDatabase();
                }
            }).start();
        }

    }

    private void seedDatabase() {
        User adminUser = new User(
                0,
                "Admin",
                "User",
                "admin@komodohub.com",
                "password123",
                "admin"
        );
        User teacherUser = new User(
                0,
                "Kakashi",
                "Hatake",
                "kakashi@komodohub.com",
                "password123",
                "teacher"
        );
        userRepository.insertUser(adminUser);
        userRepository.insertUser(teacherUser);

        Classroom classroom = new Classroom(
                0,
                "class1",
                2
        );
        classroomRepository.insertClassroom(classroom);

        User studentUser = new User(
                0,
                "Naruto",
                "Uzumaki",
                "naruto@komodohub.com",
                "password123",
                "student"
        );
        userRepository.insertUser(studentUser);

        userRepository.addUserToClassroom(3, 1);

        Species species1 = new Species(
                0,
                "Javan Rhinoceros",
                "Rhinoceros sondaicus",
                "The Javan rhinoceros is a very rare member of the rhinoceros family and one of the most endangered large mammals.",
                "Critically Endangered",
                "https://files.worldwildlife.org/wwfcmsprod/images/Javan_Rhino_7_14_2015_HERO/hero_small/mfvrot8yp__H9A0249.jpg"
        );
        Species species2 = new Species(
                0,
                "Sumatran Orangutan",
                "Pongo abelii",
                "The Sumatran orangutan is one of the three species of orangutans. Found only on the Indonesian island of Sumatra, it is rarer than the Bornean orangutan.",
                "Critically Endangered",
                "https://files.worldwildlife.org/wwfcmsprod/images/Sumatran_Orangutan/carousel_small/3o8r5hbzfl_Sumatran_Orangutan_8.6.2012_Why_They_Matter_XL_257639.jpg"
        );
        Species species3 = new Species(
                0,
                "Bali Myna",
                "Leucopsar rothschildi",
                "The Bali myna, also known as Rothschild's mynah, Bali starling, or Bali mynah, locally known as jalak Bali, is a medium-sized, stocky myna, almost wholly white with a long, drooping crest, and black tips on the wings and tail.",
                "Critically Endangered",
                "https://www.lpzoo.org/wp-content/uploads/2022/12/pre74_Portrait-175.jpg"
        );
        speciesRepository.insertSpecies(species1);
        speciesRepository.insertSpecies(species2);
        speciesRepository.insertSpecies(species3);

        ConservationActivity activity1 = new ConservationActivity(
                0,
                "Class 1 Sumatran Expedition",
                "Today we will be going on a field trip of northern sumatra to observe the Sumatran Orangutans and their habitats. You're expected to learn as much as you can and contribute towards the knowledge base of this species.",
                2
        );
        ConservationActivity activity2 = new ConservationActivity(
                0,
                "Class 1 Rhyming with the Rhinos",
                "Today we visit the Ujung Kulon National Park to get some info on the endangered Javan Rhino.",
                2
        );
        conservationActivityRepository.insertConservationActivity(activity1);
        conservationActivityRepository.insertConservationActivity(activity2);

        conservationActivityRepository.addActivityToClassroom(1, 1);
        conservationActivityRepository.addActivityToClassroom(2, 1);
    }
}
