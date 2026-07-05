TODO :: NEW FUNCTIONALITIES
===========================

* REST: `GET /routing/{origin}/{destination}`
  * **ADD** overall request processing time to the HTTP response.
  * **ADD**: Country list service request timeout: dedicated error response.
* `CountriesRestAdapter` - request timeout when calling remote service:
  * **ADD** dedicated domain exception 
* Fetch countries from remote resource:
  * **ADD**: Microservice Patterns: Resolve REST client failures.
  * Cache response for case when next call fails? 
---


TODO :: BUG FIXES
=================

---


TODO :: DEVELOPMENT
===================

* **ADD** OpenAPI REST API specification.
* **ADD** `dev` and `prod` Spring profile and appropriate configuration for them.
* Case study: Spring + Java 21: Need to explicitly enable Java Virtual Threads in Spring? 
---


TODO :: CODE MAINTENANCE
========================

* Case study: Markdown-formatted comments.
  * https://www.sonarsource.com/blog/java-23-embrace-the-new-era-of-code-comments/ 
  * "Java 23 introduces Markdown-formatted documentation comments, allowing developers to write Javadoc using familiar Markdown syntax alongside traditional HTML Javadoc tags."
  * "comments starting with /// are now officially interpreted as Javadoc comments that use Markdown syntax"
---