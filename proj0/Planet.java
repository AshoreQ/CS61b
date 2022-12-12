public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet b) {
        this.xxPos = b.xxPos;
        this.yyPos = b.yyPos;
        this.xxVel = b.xxVel;
        this.yyVel = b.yyVel;
        this.mass = b.mass;
        this.imgFileName = b.imgFileName;
    }

    /** The method is to caclculate the distance between two Plantes. */
    public double calcDistance(Planet p) {
        double dx = Math.abs(this.xxPos - p.xxPos);
        double dy = Math.abs(this.yyPos - p.yyPos);
        return Math.sqrt(dx*dx + dy*dy);
    }

    /** The method takes in a Plante,
     *  and returns a double discribing the force exerted on this Planet by the given Planet. 
     */
    public double calcForceExertedBy(Planet p) {
        double distance = this.calcDistance(p);
        return G * (this.mass) * (p.mass) / (distance * distance);
    }

    /** The method is to describe the force exerted in the X direction */
   public double calcForceExertedByX(Planet p) {
        double distance = this.calcDistance(p);
        double force = this.calcForceExertedBy(p);
        double dx = this.xxPos - p.xxPos;
        if (dx > 0) {
            return (-1.0) * (force * Math.abs(dx) / distance);
        } else {
            return force * Math.abs(dx) / distance;
        }
    }

    /** The method is to describe the force exerted in the Y direction */
    public double calcForceExertedByY(Planet p) {
        double distance = this.calcDistance(p);
        double force = this.calcForceExertedBy(p);
        double dy = this.yyPos - p.yyPos;
        if (dy > 0) {
            return (-1.0) * (force * Math.abs(dy) / distance);
        } else {
            return force * Math.abs(dy) / distance;
        }
    }

    /** Compare two planets. */
    public boolean equals(Planet p) {
        if (this.xxPos == p.xxPos && this.yyPos == p.yyPos && this.mass == p.mass && this.imgFileName == p.imgFileName) {
            return true;
        } else {
            return false;
        }
    }

    /** Take in an array of Planets and calculate the net X force exerted by all planets.  */
    public double calcNetForceExertedByX(Planet[] planets) {
        double sum = 0.0;
        for (int i = 0; i < planets.length; ++i) {
            if (!this.equals(planets[i])) {
                sum = sum + this.calcForceExertedByX(planets[i]);
            }
        }
        return sum;
    }

    /** Take in an array of Planets and calculate the net Y force exerted by all planets.  */
    public double calcNetForceExertedByY(Planet[] planets) {
        double sum = 0.0;
        for (int i = 0; i < planets.length; ++i) {
            if (!this.equals(planets[i])) {
                sum = sum + this.calcForceExertedByY(planets[i]);
            }
        }
        return sum;
    }

    /** The method is to calculate the planet's velocity and position as the result of the forces. */
    public void update(double dt, double fX, double fY) {
        double ax = fX / this.mass;
        double ay = fY / this.mass;
        this.xxVel = this.xxVel + ax * dt;
        this.yyVel = this.yyVel + ay * dt;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

     /** Draw the planet at its appropriate position */
    public void draw() {
        String fileName = "images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, fileName);
    }
}