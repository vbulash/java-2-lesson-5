public class Runner implements Runnable {
	private final float[] data;
	private int offset;

	public Runner(float[] arr, int startInSource) {
		data = arr;
		offset = startInSource;
	}

	public float[] getData() {
		return data;
	}

	@Override
	public void run() {
		System.out.println("Вычисления производятся в рамках thread: " + Thread.currentThread().getName());
		for (int index = 0; index < data.length; index++) {
			data[index] = calculate(index, data[index]);
		}
	}

	private synchronized float calculate(int index, float value) {
		return (float)(
				value *
						Math.sin(0.2f + (index + offset) / 5f) *
						Math.cos(0.2f + (index + offset) / 5f) *
						Math.cos(0.4f + (index + offset) / 2f)
		);
	}
}
