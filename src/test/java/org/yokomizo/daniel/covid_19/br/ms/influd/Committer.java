package org.yokomizo.daniel.covid_19.br.ms.influd;

import java.util.Objects;
import java.util.function.Consumer;

public interface Committer<T> extends Consumer<T> {
	void commit();

	default Committer<T> andThen(Consumer<? super T> after) {
		Objects.requireNonNull(after);
		final Committer<T> first = this;
		return new Committer<T>() {
			@Override
			public void accept(T t) {
				first.accept(t);
				after.accept(t);
			}

			@Override
			public void commit() {
				first.commit();
				if (after instanceof Committer<?>) {
					((Committer<?>) after).commit();
				}
			}
		};
	}
}