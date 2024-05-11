package models;

import lombok.Data;
import java.util.Date;
@Data
public class UserDataResModel {
    String name, job, email, id;
    public Date createdAt;
}
