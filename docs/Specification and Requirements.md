Service Specification
=====================

Functional Requirements
-----------------------

* Calculate any possible land route from one country to another.
* Take a list of country data in JSON format:
    * https://raw.githubusercontent.com/mledoze/countries/master/countries.json
* Calculate the route by utilizing individual countries border information.
* Single route is returned if the journey is possible.
* Countries are identified by `cca3` field in country data.
* Route calculation algorithm needs to be efficient.
* Expose REST endpoint `/routing/{origin}/{destination}`:
    * Returns a list of border crossings to get from origin to destination.
    * Origin and destination are included in the returned list.
    * If there is no land crossing, the endpoint returns `HTTP 400 Bad Request`.

HTTP request example: \
`GET /routing/CZE/ITA`

HTTP response example:

```json
{
  "route": [
    "CZE",
    "AUT",
    "ITA"
  ]
}
```

Non-Functional Requirements
---------------------------

### Implementation Requirements

* Spring Boot service
* Java
* Maven