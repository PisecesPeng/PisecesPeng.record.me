## Java, Spliterator使用示例

- [Java, Spliterator使用示例](#java-spliterator使用示例)
  - [1. Stream.parallel使用示例](#1-streamparallel使用示例)
  - [2. Spliterator使用示例](#2-spliterator使用示例)
  - [3. 自定义Spliterator示例](#3-自定义spliterator示例)

> Spliterator是什么 :  
> 1. Spliterator是一个接口.
> 2. Spliterator对Stream实例化很重要(StreamSupport.stream()).
> 3. Spliterator通常和Stream一起使用, 用来遍历和分割序列.
> 4. ParallelStream实现并行, 依赖于Spliterator接口.

---

### 1. Stream.parallel使用示例

```java
List<Integer> list = Stream.iterate(0, item -> item + 1).limit(10000).collect(Collectors.toList());

int sum1 = list.stream().mapToInt(Integer::new).sum();
int sum2 = list.stream().reduce(0, (a, b) -> a + b);
// sum1 : 49995000, sum2 : 49995000

int sum3 = list.parallelStream().mapToInt(Integer::new).sum();
// sum3 : 49995000
```

### 2. Spliterator使用示例

```java
List<Integer> list = Stream.iterate(0, item -> item + 1).limit(100).collect(Collectors.toList());

// s1: 0~99
Spliterator<Integer> s1 = list.spliterator();
// s1: 50~99, s2: 0~49
Spliterator<Integer> s2 = s1.trySplit();
// s1: 25~49, s2: 51~100, s3: 0~24
Spliterator<Integer> s3 = s1.trySplit();
// s1: 25~49, s2: 25~49, s3: 0~24, s4: 0~24
Spliterator<Integer> s4 = s2.trySplit();

Thread t1 = new Thread(() -> s1.forEachRemaining(x -> System.out.println(x)));
Thread t2 = new Thread(() -> s2.forEachRemaining(x -> System.out.println(x)));
Thread t3 = new Thread(() -> s3.forEachRemaining(x -> System.out.println(x)));
Thread t4 = new Thread(() -> s4.forEachRemaining(x -> System.out.println(x)));

t1.start();
t2.start();
t3.start();
t4.start();

while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive()) {
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        // do nothing...
    }
}
System.out.println("end");
```

### 3. 自定义Spliterator示例

```java
/**
 * 单词计数工具
 */
class WordCounter {

    private final int counter;
    private final boolean lastSpace;

    public WordCounter(int counter, boolean lastSpace) {
        this.counter = counter;
        this.lastSpace = lastSpace;
    }

    public WordCounter accumulate(Character c) {
        // 当且仅当 前一字符为空格, 而当前字符非空格, 则单词数量就加1
        if (Character.isWhitespace(c))
            return lastSpace ? this : new WordCounter(counter, true);
        else
            return lastSpace ? new WordCounter(counter + 1, false) : this;
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }

    public int getCounter() {
        return counter;
    }

    public boolean isLastSpace() {
        return lastSpace;
    }

}
```
```java
/**
 * 自定义单词计数拆分类
 */
class WordCounterSpliterator implements Spliterator<Character> {

    private final String string;

    private int currentChart = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    /**
     * 该方法会处理每个元素, 如果没有元素处理, 则应该返回false(中断Stream处理), 否则返回true
     * <p>
     * ps.
     * {@link Stream#reduce}reduce操作的accumulate方法最终将调用到{@link #forEachRemaining(Consumer)}
     * 从而将{@link Consumer}传递到{@link #tryAdvance(Consumer)}中的Consumer.accept调用
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> consumer) {
        consumer.accept(string.charAt(currentChart++));
        return currentChart < string.length();
    }

    /**
     * 该方法会对现有的stream进行分拆(一般用在parallelStream的情况),
     * 当该方法返回null的时候, 说明当前已不能再进行分割, 就会调用顺序处理{@link #forEachRemaining}.
     */
    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChart;
        if (currentSize < 10) {
            return null;
        }
        // 以空格做为拆分点
        for (int splitPos = currentSize / 2 + currentChart; splitPos < string.length(); splitPos++) {
            if (Character.isWhitespace(string.charAt(splitPos))) {
                Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChart, splitPos));
                currentChart = splitPos;
                return spliterator;
            }
        }
        return null;
    }

    /**
     * 表示Spliterator中待处理的元素
     */
    @Override
    public long estimateSize() {
        return string.length() - currentChart;
    }

    /**
     * 返回数字, 代表Spliterator本身的特性的编码
     * <p>
     * ORDERED	    元素有既定的顺序(例如{@link List}), 因此{@link Spliterator}在遍历和划分时也会遵循这一顺序
     * DISTINCT	    对于任意一对遍历过的元素x和y, x.equals(y)返回false
     * SORTED	    遍历的元素按照一个预定义的顺序排序
     * SIZED	    该{@link Spliterator}由一个已知大小的源建立(例如{@link Set}), 因此{@link #estimateSize()}返回的是准确值
     * NONNULL	    保证遍历的元素不会为null
     * IMMUTABLE	{@link Spliterator}的数据源不能修改. 这意味着在遍历时不能添加、删除或修改任何元素
     * CONCURRENT	该{@link Spliterator}的数据源可以被其他线程同时修改而无需同步
     * SUBSIZED	    该{@link Spliterator}和所有从它拆分出来的Spliterator都是SIZED
     */
    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }

}
```
```java
public static void main(String[] args) {
    String words = "a b c d e";

    Stream<Character> stream1 = IntStream.range(0, words.length()).mapToObj(words::charAt);
    int count = count(stream1);
    System.out.println("单词数量为：" + count);

    Spliterator<Character> spliterator = new WordCounterSpliterator(words);
    Stream<Character> stream2 = StreamSupport.stream(spliterator, true);
    int count2 = count(stream2);
    System.out.println("单词数量为：" + count2);

}

public static int count(Stream<Character> stream) {
    WordCounter wordCounter = stream.reduce(
            new WordCounter(0, true),
            WordCounter::accumulate,
            WordCounter::combine
    );
    return wordCounter.getCounter();
}
```