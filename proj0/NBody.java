public class NBody {
    public static String imageToDraw = "images/starfield.jpg";
    /** Return the radius of the universe. */
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int numberOfPlanets = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    /** Return an array of Planets corresponding to 
     *  the planets in the file.
     */
     public static Planet[] readPlanets(String fileName) {
        int sum = 0;
        In in = new In(fileName);
        int numberOfPlanets = in.readInt();
        double radius = in.readDouble();
        
        Planet[] planets = new Planet[numberOfPlanets];

        for(int i = 0; i < planets.length; ++i) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String imgFileName = in.readString();
            Planet p = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
            planets[i] = p;
        }
        return planets;
     }


     public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        for(int i = 0; i < planets.length; ++i) {
            planets[i].draw();
        }

        StdDraw.enableDoubleBuffering();
        double[] xForces = new double[planets.length];
        double[] yForces = new double[planets.length];

        for(int t = 0; t < T; ++t) {
            for(int i = 0; i < planets.length; ++i) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for(int i = 0; i < planets.length; ++i) {
                planets[i].update(t, xForces[i], yForces[i]);
            }

            StdDraw.setScale(-radius, radius);
            StdDraw.clear();
            
            for(int i = 0; i < planets.length; ++i) {
                planets[i].draw();
            }

            StdDraw.show();
            StdDraw.pause(10);
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }
}