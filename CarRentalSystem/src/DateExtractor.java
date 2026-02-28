class DateExtractor{

    public static int extractDay(String date) {
        String [] newDate = date.split("-");
        return Integer.parseInt(newDate[newDate.length - 1]);
    }
}
