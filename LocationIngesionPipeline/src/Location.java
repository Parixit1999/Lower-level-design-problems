class Location {
    Long lat;
    Long lg;

    @Override
    public boolean equals(Object obj) {
        return this.lat == ((Location) obj).lat && this.lg == ((Location) obj).lg;
    }
}
