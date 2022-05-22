import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Main {
	static final int size = 100_000_000;
	static final int h = size / 2;

	public static float @NotNull [] execute(boolean parallel) {
		float[] arr = new float[size];
		Arrays.fill(arr, 1.0f);
		//
		long start = System.currentTimeMillis();
		if (parallel) {
			parallel(arr);
		} else {
			Runner runner = new Runner(arr, 0);
			runner.run();
		}
		//
		long end = System.currentTimeMillis();
		System.out.println((parallel ? "Двухпоточное" : "Однопоточное") + " выполнение, миллисекунд: " + (end - start));
		System.out.println();

		return arr;
	}

	public static void parallel(float @NotNull [] arr) {
		// Разделить исходный массив пополам
		int indexHalf = arr.length / 2;
		float[] leftPart = new float[indexHalf];
		float[] rightPart = new float[indexHalf];
		System.arraycopy(arr, 0, leftPart, 0, indexHalf);
		System.arraycopy(arr, indexHalf, rightPart, 0, indexHalf);

		// Обработать оба массива в параллельных потоках
		Runner leftRunner = new Runner(leftPart, 0);
		Runner rightRunner = new Runner(rightPart, indexHalf);

		Thread leftThread = new Thread(leftRunner);
		Thread rightThread = new Thread(rightRunner);

		leftThread.start();
		rightThread.start();
		try {
			leftThread.join();
			rightThread.join();
		} catch (InterruptedException exc) {
			System.out.println("Выполнение процесса прервано");
		}

		// Обратная склейка в массив
		System.arraycopy(leftPart, 0, arr, 0, indexHalf);
		System.arraycopy(rightPart, 0, arr, indexHalf, indexHalf);
	}

	public static void main(String[] args) {
		float[] sequential = Main.execute(false);	// Последовательное выполнение
		float[] parallel = Main.execute(true);		// Параллельное (двухпоточное) выполнение

		System.out.println(Arrays.equals(sequential, parallel) ? "Массивы идентичны" : "Массивы не идентичны");
	}
}