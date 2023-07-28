package com.example.secondCertification.models;

import com.example.secondCertification.models.answers.ApiAnswer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TicketModel {
    private static final DateFormat FORMAT = new SimpleDateFormat("dd-MM-yyyy"); // формат даты, который ждём из json
    /** Проверка что дата закрытия после даты создания
     *
     * @param dateFirst дата создания билета
     * @param dateSecond дата закрытия билета
     * @return
     */
    public static boolean isCurrentDate(String dateFirst, String dateSecond){
        try { // пытаемся распарсить то, что пришло и проверяем
            if ( FORMAT.parse(dateSecond).after(FORMAT.parse(dateFirst))){
                return true;
            }
        } catch (ParseException e) { // пришло не то, что ждали
            return false;
        }
        return false; // распарсили успешно, но закрытие после создания
    }

    public static String increaseDate(String date, int count){
        String increaseDate= "";
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(FORMAT.parse(date));
            calendar.add(Calendar.DATE, count);
            increaseDate = FORMAT.format(calendar.getTime());
        }catch (ParseException e) { // пришло не то, что ждали
            return increaseDate; // возвращаем пустую строку
        }
        return increaseDate;
    }

    public static ResponseEntity<?> badToken(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.NOT_FOUND.value(),
                "Tocken is null."),
                HttpStatus.NOT_FOUND);
    }
    public static ResponseEntity<?> missingToken(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.NOT_FOUND.value(),
                "This token missing from session."),
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> badTicket(){
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                "Ticket is not created."),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> ticketDelete() {
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.OK.value(),
                "Ticket is deleted"),
                HttpStatus.OK);
    }

    public static ResponseEntity<?> ticketNotDelete() {
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                "Ticket is not deleted. Check the data"),
                HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> updateTicket() {
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.CREATED.value(),
                "Ticket is updated."),
                HttpStatus.CREATED);
    }

    public static ResponseEntity<?> ticketNotUpdate() {
        return new ResponseEntity<>(new ApiAnswer(HttpStatus.BAD_REQUEST.value(),
                "Ticket is not updated. Check the data"),
                HttpStatus.BAD_REQUEST);
    }

}
