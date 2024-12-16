import java.util.Random;

public class PSO
{

    static final int NUM_PARTICLES = 100;
    static final int MAX_ITERATIONS = 1000;
    static final double ALPHA = 0.5; //wartość alfa
    static final double BETA = 0.3;  //wartość beta
    static final double VELOCITY_MIN = -2;
    static final double VELOCITY_MAX = 2;
    static final double POSITION_MIN = -10;
    static final double POSITION_MAX = 10;

    static Random random = new Random();

    //klasa cząstki do przechowywania pozycji, prędkości i oceny jakości
    static class Particle
    {
        double x, y;       // Pozycja
        double vX, vY;     // Prędkość
        double personalBestX, personalBestY;
        double personalBestValue;

        public Particle()
        {
            this.x = randomInRange(POSITION_MIN, POSITION_MAX);
            this.y = randomInRange(POSITION_MIN, POSITION_MAX);
            this.vX = randomInRange(VELOCITY_MIN, VELOCITY_MAX);
            this.vY = randomInRange(VELOCITY_MIN, VELOCITY_MAX);
            this.personalBestX = x;
            this.personalBestY = y;
            this.personalBestValue = evaluateFunction(x, y);
        }
    }

    //funkcja celu
    static double evaluateFunction(double x, double y)
    {
        return Math.sin(Math.pow(x, 4)) - Math.cos(Math.pow(y, 2)) + 6 * Math.pow(x, 2) * Math.pow(y, 2);
    }

    //generowanie losowej liczby z  określonego zakresu
    static double randomInRange(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }

    public static void main(String[] args)
    {
        Particle[] particles = new Particle[NUM_PARTICLES];
        double globalBestX = 0, globalBestY = 0;
        double globalBestValue = Double.MAX_VALUE;

        //inicjalizacja cząstek
        for (int i = 0; i < NUM_PARTICLES; i++)
        {
            particles[i] = new Particle();
            if (particles[i].personalBestValue < globalBestValue)
            {
                globalBestX = particles[i].personalBestX;
                globalBestY = particles[i].personalBestY;
                globalBestValue = particles[i].personalBestValue;
            }
        }

        //główna pętla PSO
        for (int t = 0; t < MAX_ITERATIONS; t++)
        {
            for (Particle p : particles) {
                //generowanie epsilonów
                double epsilon1 = random.nextDouble();
                double epsilon2 = random.nextDouble();

                //aktualizacja prędkości
                p.vX = p.vX + ALPHA * epsilon1 * (globalBestX - p.x) + BETA * epsilon2 * (p.personalBestX - p.x);
                p.vY = p.vY + ALPHA * epsilon1 * (globalBestY - p.y) + BETA * epsilon2 * (p.personalBestY - p.y);

                //ograniczenie prędkości do zakresu
                p.vX = Math.max(VELOCITY_MIN, Math.min(VELOCITY_MAX, p.vX));
                p.vY = Math.max(VELOCITY_MIN, Math.min(VELOCITY_MAX, p.vY));

                //aktualizacja pozycji
                p.x += p.vX;
                p.y += p.vY;

                //ograniczenie pozycji do zakresu
                p.x = Math.max(POSITION_MIN, Math.min(POSITION_MAX, p.x));
                p.y = Math.max(POSITION_MIN, Math.min(POSITION_MAX, p.y));

                //ocena wartości funkcji celu
                double currentValue = evaluateFunction(p.x, p.y);

                //aktualizacja najlepszego wyniku osobistego
                if (currentValue < p.personalBestValue)
                {
                    p.personalBestX = p.x;
                    p.personalBestY = p.y;
                    p.personalBestValue = currentValue;
                }

                //aktualizacja najlepszego globalnego wyniku
                if (currentValue < globalBestValue)
                {
                    globalBestX = p.x;
                    globalBestY = p.y;
                    globalBestValue = currentValue;
                }
            }

            //wyświetlenie postępu co 100 iteracji
            if (t % 100 == 0)
            {
                System.out.printf("Iteracja %d: Najlepsza globalna wartość = %.6f w punkcie (%.4f, %.4f)%n", t, globalBestValue, globalBestX, globalBestY);
            }
        }

        System.out.printf("\nOstateczna najlepsza globalna wartość = %.6f w punkcie (%.4f, %.4f)%n", globalBestValue, globalBestX, globalBestY);
    }
}
