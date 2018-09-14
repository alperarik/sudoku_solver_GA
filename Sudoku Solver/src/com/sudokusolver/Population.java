package com.sudokusolver;

public class Population {

	private PopulationElement[] population; // array of population elements create population
	private int popSize; // size of population

	/**
	 * Population constructor
	 *
	 * @param memberSize size of elements
	 * @param popSize number of elements of population
	 */
	public Population(int memberSize, int popSize) {
		this.popSize = popSize;
		population = new PopulationElement[popSize + 1];
		for (int i = 0; i < population.length; i++) {
			population[i] = new PopulationElement(memberSize);
		}
	}

	public int getSize() {
		return popSize;
	}

	public PopulationElement getMember(int i) {
		return population[i];
	}

	/**
	 * selects random element
	 *
	 * @return random population element
	 */
	public PopulationElement selectRandomElement() {
		int x = (int) (popSize * Math.random());
		return population[x];
	}

	/**
	 * Insert element to population which is sorted by score
	 *
	 * @param x index of element
	 * @return index of inserted element
	 */
	public int insertElement(int x) {

		PopulationElement temp = population[x];
		x--;
		// put into the right index
		while (x >= 0 && population[x].getScore() < temp.getScore()) {
			population[x + 1] = population[x];
			x--;
		}
		population[x + 1] = temp;

		return x + 1;
	}

	public void sort() {
		PopulationElement tempElement;
		int i, j;

		for (i = 1; i < population.length; i++) {
			for (j = 0; j < population.length - i; j++) {
				if (population[j].getScore() < population[j + 1].getScore()) {
					tempElement = population[j];
					population[j] = population[j + 1];
					population[j + 1] = tempElement;
				}
			}
		}
	}

}
