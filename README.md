# Collision Detection Library

This repository contains a Java library for collision detection in geometric shapes. It provides classes and methods to determine whether various geometric shapes intersect with each other.

## Table of Contents

- [Background](#background)
- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Contributing](#contributing)
- [License](#license)

## Background

The collision detection library includes implementations for the following geometric shapes:

- **Point**: Represents a point in 2D space.
- **Line Segment**: Represents a line segment defined by two points.
- **Rectangle**: Represents a rectangle defined by its top-left and bottom-right corners.
- **Circle**: Represents a circle defined by its center and radius.

The library provides methods to check for intersections between these shapes.

## Installation

To use this library in your Java project, you can clone this repository and include the necessary classes in your project's source directory. Alternatively, you can compile the source files into a JAR file and include it in your project's dependencies.

## Usage

To use the collision detection library, import the necessary classes into your Java code. You can then create instances of geometric shapes and use the provided methods to check for intersections.

For example, to check if a point intersects with a rectangle:

```java
Point point = new Point(2, 3);
Rectangle rectangle = new Rectangle(0, 0, 5, 5);

if (rectangle.intersect(point)) {
    System.out.println("Point intersects with rectangle.");
} else {
    System.out.println("Point does not intersect with rectangle.");
}
```
## Examples

The repository includes examples demonstrating various use cases of the collision detection library. You can find these examples in the main.java file.

## Contributing

Contributions to this project are welcome! If you have any ideas for improvements or new features, feel free to open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
