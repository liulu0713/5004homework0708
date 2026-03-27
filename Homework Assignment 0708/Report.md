# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. Describe the purpose of a model in the MVC architecture. What is the model responsible for? What are some examples of what might be included in a model?

Model represents the application's data and the logic governing that data.

2. Describe the purpose of a controller in the MVC architecture. What is the controller responsible for? What are some examples of what might be included in a controller?
Controller acts as the intermediary between the User, the Model, and the View.

3. Describe the word serialization, and how it relates to 'data-binding' in Jackson. What is the purpose of serialization? What is the purpose of data-binding? What is the relationship between the two?
Serialization: The process of converting an in-memory object into a format that can be stored or transmitted (e.g., converting a Java Object to a JSON string).

Data-Binding: The process of mapping data from one format (like JSON or XML) directly onto a software object (POJO) and vice-versa.

Relationship: Serialization is the mechanical act of transformation, while data-binding is the high-level mapping strategy. In Jackson, data-binding uses serialization to turn your Java classes into JSON output.

4. Describe the differences between JSON and CSV - make sure to reference flat or hierarchical data in your answer. What are some advantages of JSON over CSV? What are some disadvantages?
JSON supports nested structures, arrays, and complex relationships.
CSV  represents data in a simple grid of rows and columns.
5. Why would we want to use InputStreams and OutputStreams throughout a program instead of specific types of streams (for most cases)?
   Using the base InputStream and OutputStream classes allows for polymorphism.
   By writing code that accepts these general types,program doesn't care if the data is coming from a file, 
   a network socket, or a byte array. This makes your code more modular and easier to test.

6. What is the difference between a record and a class in Java? When might you use one over the other?
   Record: A special type of class intended to be a simple transparent carrier for immutable data. 
   It automatically generates getters, equals(), hashCode(), and toString(). Use this for "Data Transfer Objects" (DTOs).

Class: A full-featured blueprint for objects that can hold mutable state and complex behavior.
Use this when you need encapsulation or need to change values after the object is created.

7. What is a java "bean"?
 Jaca bean is a standard for creating reusable classes. The class must have a public,no argument constructor.

## Deeper Thinking

The data for this assignment was downloaded from ipapi.co, and the data itself is publicly listed. For the pervious assignment, we used data from Board Game Geek, which a person has a unofficial collection of the BGArena games. It is also worth noting it is actually out of date, since BGGeek has added a category for Digital Implementations of games.

Data of many types are  often available online (Here is a list of a bunch of [free API](https://mixedanalytics.com/blog/list-actually-free-open-no-auth-needed-apis/)s), and even the owners of Board Game Geek have RPGGeek and VideoGameGeek. To try out Board Game geeks API, you can put into the url https://www.boardgamegeek.com/xmlapi2/thing?id=13 and you will get back an XML file with information about the game with id 13. 

Take some time to find other online data APIs that you might be interested in. What kind of data would you like to work with? What kind of data would be useful for you to have access to? Another example of an API a random dog image API https://dog.ceo/dog-api/!


🔥 Find at least two other APIs/Data sources (so downloadable data is also valid). Link them, and provide a brief description of what kind of data they provide. These will act as your references for this assignment.
Dog CEO’s Dog API
Provides random dog images and breed information.

Open Library API (Educational & Media Data)
Access to millions of book records, author metadata, and cover images.


However, just because information is freely available online, it does not mean

* You have legal rights to that information
* The information was collected ethically
* The information is accurate
* The information is without bias
  
🔥 Take some time to think about the ethical implications of using data from the internet. What are some ways that you can ensure that the data you are using is accurate and unbiased? Provide some examples of key questions you might ask yourself before using data in a project, and what are some questions you can use to help you evaluate the data you are using. Please include references if you use any, as there are plenty of articles out there talking about this topic.
Just because data is "free" does not mean it is "clean" or "fair." When using external data, I consider the following:
Was this data gathered with the consent of the individuals involved?
Is this data is up to date?
Investigate who collected the data and why.