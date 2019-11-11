package com.project.android_kidstories.data.source.local.relational;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.project.android_kidstories.data.model.ReadStory;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.model.User;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 10/10/19
 */


@androidx.room.Database(entities = {Story.class, User.class, ReadStory.class}, version = 4, exportSchema = false)
public abstract class StoryDatabase extends RoomDatabase {
    private static StoryDatabase INSTANCE;

    public static synchronized StoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            return Room.databaseBuilder(context, StoryDatabase.class, "story_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract StoryDao storyDao();
    public abstract UserDao userDao();
    public abstract ReadStoryDao readStoryDao();
}

