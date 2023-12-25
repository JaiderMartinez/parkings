package co.com.parking.r2dbc.utils;

public class Constant {

    private Constant() {}

    public static final String QUERY_FIND_OPEN_RESERVED_SPACE_IN_A_PARKING =
                    "SELECT * FROM reserve_space " +
                    "INNER JOIN parking_spaces " +
                    "ON reserve_space.id_parking_space = parking_spaces.id " +
                    "INNER JOIN parkings " +
                    "ON parking_spaces.id_parking = parkings.id " +
                    "WHERE reserve_space.id_user = :idUser " +
                    "AND parkings.id = :idParking " +
                    "AND reserve_space.id_parking_space = :idParkingSpace " +
                    "AND reserve_space.reservation_end_date IS NULL";
    public static final String PATH_GET_USER_BY_ID = "user/{idUser}";
}
