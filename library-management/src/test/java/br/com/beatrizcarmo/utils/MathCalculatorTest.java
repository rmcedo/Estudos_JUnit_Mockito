package br.com.beatrizcarmo.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class MathCalculatorTest {

	MathCalculator mathCalculator;

	@Parameterized.Parameter(0)
	public int param1;

	@Parameterized.Parameter(1)
	public int param2;

	@Parameterized.Parameter(2)
	public int param3;

	@Parameterized.Parameters
	public static Collection<Object[]> parameters() {
		var obj = new Object[][] { { 3, 2, 5 }, { 6, 4, 10 }, { 7, 2, 9 } };
		return Arrays.asList(obj);
	}

	@Before
	public void setUp() {
		mathCalculator = new MathCalculator();
	}

	@Test
	public void add_ShouldAddBothNumbers() {
		var num1 = param1;
		var num2 = param2;

		var soma = mathCalculator.add(num1, num2);

		assertThat(param3).isEqualTo(soma);
	}

	@Test(expected = ArithmeticException.class)
	public void add_ArithmeticException() {

		int x = Integer.MAX_VALUE;
		int y = 1;

		mathCalculator.add(x, y);
	}
}
