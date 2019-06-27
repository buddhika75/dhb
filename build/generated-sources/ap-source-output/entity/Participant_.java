package entity;

import entity.Gender;
import entity.Institution;
import entity.Room;
import entity.RoomType;
import entity.WorkGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-27T13:09:26")
@StaticMetamodel(Participant.class)
public class Participant_ { 

    public static volatile SingularAttribute<Participant, WorkGroup> workgroup;
    public static volatile SingularAttribute<Participant, String> fileName;
    public static volatile SingularAttribute<Participant, Gender> gender;
    public static volatile SingularAttribute<Participant, String> signature;
    public static volatile SingularAttribute<Participant, Boolean> secondDay;
    public static volatile SingularAttribute<Participant, Room> room;
    public static volatile SingularAttribute<Participant, Institution> institution;
    public static volatile SingularAttribute<Participant, Boolean> firstDay;
    public static volatile SingularAttribute<Participant, String> phone;
    public static volatile SingularAttribute<Participant, Boolean> overnightStay;
    public static volatile SingularAttribute<Participant, String> name;
    public static volatile SingularAttribute<Participant, Long> id;
    public static volatile SingularAttribute<Participant, String> designation;
    public static volatile SingularAttribute<Participant, byte[]> baImage;
    public static volatile SingularAttribute<Participant, String> email;
    public static volatile SingularAttribute<Participant, RoomType> roomType;
    public static volatile SingularAttribute<Participant, String> fileType;

}