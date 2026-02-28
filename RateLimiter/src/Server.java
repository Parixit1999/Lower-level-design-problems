class Server {
    private RateLimiter rateLimiter;

    public Server(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public boolean request(Identity u) {
        return this.rateLimiter.isAllow(u);
    }
}
