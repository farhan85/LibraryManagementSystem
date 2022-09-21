# Library Management System

A small project I'm writing in my spare time to demonstrate some coding design patterns I've learnt over the years. This
application is a simple library management system that (eventually) can be used to keep a record of all the books in a
library, the current stock and which books have been borrowed.

# My philosophy on coding

There are different ways to approach coding, and the situations determine which approach makes more sense. In some
cases you need to optimise for speed and memory usage, in other cases shaving off one extra second doesn't give you
much because the entire system takes a few minutes to complete its computation, or network calls are the actual
bottlenecks. As more data gets processed, more memory and storage is used, and we must choose between scaling up,
reducing memory usage, or scaling out horizontally. In a professional setting, developer time is probably the most
precious commodity. If the developers are too busy debugging code and re-reading super convoluted, tightly coupled,
somewhat cryptic unreadable code, then time is being lost for the more appealing feature work.

During my 10+ years as a software engineer I have increasingly found myself putting more value in making code easy to
debug, and harder to introduce bugs. When I first learnt programming, I did what everyone does the first time they learn
something new: practice using it all the time. I wrote lots of code, created parent/child classes and overloaded methods
everywhere. It wasn't long before I came crashing back down and learnt the hard way how reading old code is not easy,
that comments always fall behind, missing logs makes debugging much harder, why composition is better than inheritance
and why it's easy to miss unit test cases when your classes being tested are way too big. I have also seen myself over
the years write smaller and smaller methods, reaching to the point where I now have an aversion to nested indentations
in a method. The more my method looks like a flat list of instructions (do this first, then do that, then do this...)
the easier it is for me to know, at a high level, what my method is doing and why.

That is the context behind my philosophy of coding. My aim is not to write the most optimal piece of assembly code
ever, but to make coding and even debugging more fun and easy for me and my future team members. If you see yourself
in the same boat, then read on. Hopefully you'll find my approaches to coding can also be applied to yours.

## Defensive coding

What is our number one priority when writing code? Maybe sometimes it's to solve the problem at hand using the best
algorithm possible. But if there is one thing we can be certain about in every project, it is that there will always
be errors we need to debug. Debugging takes _a lot_ of time and slows down every project. The longer I spent working
on a project or maintaining a service, the more I wished the time spent debugging did not take so long. Band-aid fixes
were not enough. True, it would fix the particular problem I was debugging, but it did not guarantee I would run into
the same problem again. Writing clean code can reduce bugs, but having the mindset that you want to solve the class of
problems behind the root cause of the issue will also move you faster towards a stable system.

I have had many "healthy" discussions about writing optimal code. Some people were horrified that I chose to add more
method calls, or inject more classes. But for the situations that I was usually writing code for, choosing safety over
speed simply made more sense to me. By taking this approach to coding, I try to write code that makes it difficult to
introduce bugs, and easy to discover root causes of errors. There are many ways to achieve this

- Keeping class sizes small (single responsibility) then it is easier to reason it is doing the right thing, and it is
  easier to know if you have missed a test case in your unit test
- Using descriptive names to reduce a reliance on comments and making it easier to understand the logic behind your code
- Using interfaces and encapsulation to write logic at a higher level, and leave the low level details in their own (
  single responsible) classes and methods
- Writing unit tests that are self-contained and have no effect on other tests. Once they are testing a particular use
  case, they must always test that same use case
- Write lots of logs
- And many more...

Anyone can write code that completes a task. But it's the best of us that can write code that makes maintenance and
extension easy and fun for our future colleagues that will also work on the same code base.

## Single responsibility

If I had to pick a design principle to be ranked the number one most important, it would be _Single Responsibility_.
Officially it means a module should be responsible for one, and only one, action. This applies to classes and methods.
Especially methods. If a method has only one responsibility, then it's name will accurately describe what it is used for
and you now have a self-commenting method.

From my personal experience, the two biggest benefits of Single Responsibility are 1) It's easier to reason about your
class/method, and 2) It's easier to write unit tests. If your class/method truly does only one thing, then it should be
easy to read it and know all possible code paths it can take. If the class is small, then its unit test class should
have only 2 or 3 test methods. The more methods you have to put in your unit test class, the harder it will be to notice
if there are any missing test cases.

Suggestions on adhering to the Single Responsibility principle:

- Each Class should be responsible for only one piece of work. To help enforce this, each class should have only one
  public method.
- If you need a class to perform multiple logical steps, use composition to inject other objects (that each have their
  own single responsibility) and delegate work to them.
- Classes should be small. Try to make classes small enough to fit on the screen so that scrolling is not required.
- If you're writing nested if statements, consider moving the inner logic to another helper method or class. Evey time
  you have a nested if statement or loop, you are switching contexts. It's easier to understand code when reading a
  method (months after you've written it) if it only has to deal with one context.

One last suggestion, which does not technically fall under Single Responsibility, is to make the top-level public
method not contain any loops. Make it a "list of instructions" (do this, then do that...) and move any loops to a
private helper method. When you need to read this class later in the future, the top level method should be easier to
understand what the class is doing (without needing to read the exact/low-level details yet).

## Immutable POJOs

### Why immutable objects and no setters?

## Nulls and Optional values

## Value objects

## Constructors and Getters examples

## Inheritance vs Composition

## Writing logs

This one is not quite a design standard, but something I had a long time to think about over the years of debugging.
Some people will write logs using the smallest characters possible, like `x=123`. Others will write logs that use more
natural language. Which makes sense. People read logs when debugging. So it should be easy to read.

Firstly, logs have to be very descriptive. We read logs before diving into our code. Our logs must tell us what happened
and it should be easy to know this just from reading the log entry. Don't use shortened words or abbreviations. It's no
significant extra work for a computer to write it out.

I have had to read many log files with well over tens of thousands of lines. They were so big we had to use `grep` to
search for entries (or use other solutions like AWS CloudWatch Logs). We would write queries to search for log entries,
and to also extract data to gather information about the impact. To handle both cases, my preference is now to write
logs in the following format:

```text
<Description of issue> - <context>

// Example
Could not add book to list because of invalid subject. BookId=... BookSubject=... ExpectedSubject=... 
```

When logs are written in this format, you simply search on the description, which is a fixed string, and then extract
from the key/value pairs.

Alternatively, log in JSON format, and use an application that parses the log for you, and makes querying easier.

### Logging for easy reading vs logging for easy parsing

Most people write logs that are easy for humans to read. For example:

```text
// Java code
log.error("Could not add book {} to list because its subject {} was not Mystery", book.getId(), book.getSubject());

// Log entry
Could not add book 123 because its subject Fantasy was not Mystery
```

Usually we look at logs are when things go wrong. For the example above, you may find yourself writing the following
grep command:

```shell
> grep 'Could not add book' application.log
```

But what if there were other similar log entries:

```text
Could not add book 123 because its subject Fantasy was not Mystery
Could not add book 456 because its title did not match filter
Could not add book 789 because its subject Fantasy is not allowed in this order
Could not add book 321 because its subject Thriller is not allowed in this order
```

Then the query would need to be updated with an additional filters

```shell
> grep 'Could not add book' application.log | grep 'because its subject' | grep 'was not'
```

And how would we now find all the subjects whose lists were not being populated correctly?

```shell
> grep 'Could not add book' application.log \
| grep 'because its subject' \
| grep 'was not' \
| awk '{print $NF}' \
| sort \
| uniq
```

Notice how the grep queries have to account for the subtle differences in each log entry. If we instead used the
description/context format from above:

```text
Book not added because of invalid subject. BookId=123 BookSubject=Fantasy ExpectedSubject=Mystery
Book not added because title does not match filter. BookId=456 Title=<some book title>
Book not added because subject is not allowed in order. BookId=789 Subject=Fantasy OrderNo=1234
Book not added because subject is not allowed in order. BookId=321 Subject=Thriller OrderNo=5678
```

Then the query would simply be 1) Search for the fixed string, 2) Extract the key/val:

```shell
> grep 'Book not added because of invalid subject' application.log \
| grep -o 'BookSubject=[^ ]\+' \
| cut -d= -f2 
```

## Unit tests

### Method naming conventions

## Consistency