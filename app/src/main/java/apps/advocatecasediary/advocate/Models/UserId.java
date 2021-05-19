package apps.advocatecasediary.advocate.Models;

import androidx.annotation.NonNull;

public class UserId {
    public  String userId;

    public <T extends apps.advocatecasediary.advocate.Models.UserId> T withId(@NonNull final String id){
        this.userId = id;
        return (T) this;
    }
}
