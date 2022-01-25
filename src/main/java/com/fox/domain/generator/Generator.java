package com.fox.domain.generator;

import java.util.List;

public interface Generator<T> {

    List<T> generate(int number);

}
