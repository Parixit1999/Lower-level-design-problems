class Location implements Comparable<Location>{

    long lat;
    long lg;

    @Override
    public int compareTo(Location o) {
        return Long.compare(this.lat, o.lat) + Long.compare(this.lg,  o.lg);
    }
}
