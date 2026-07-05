REST API v1.0
=============

REST API documentation.

API Version: **1.0**

`GET` `/routing/{origin}/{destination}`
=======================================

Calculate route from `origin` to `destination` country.

## HTTP Request

| Path parameter |                          |
|----------------|--------------------------|
| `origin`       | The source country.      |
| `destination`  | The destination country. |

## HTTP Response

### HTTP Base response

Common fields for `200 OK` and `400 Bad Request`:

```json
{
  "hasError": false,
  "apiVersion": "1.0",
  "timestamp": "2026-07-06T15:00:23.923578"
}
```

| JSON Field   | Type      | Description                                                                       |
|--------------|:----------|-----------------------------------------------------------------------------------|
| `hasError`   | `boolean` | *Future feature.* Indicates, that `ErrorDescription` is included. Always `false`. |
| `apiVersion` | `string`  | API version.                                                                      |
| `timestamp`  | `string`  | Timestamp in UTC time zone.                                                       |

### HTTP Response `200 OK`

* Route was found.
* Calculated route.

```json
{
  "origin": "pol",
  "destination": "aut",
  "route": [
    "POL",
    "SVK",
    "AUT"
  ],
  "hasError": false,
  "apiVersion": "1.0",
  "timestamp": "2026-07-06T15:00:23.923578"
}
```

| JSON Field    | Type                | Description                                                                                        |
|---------------|---------------------|----------------------------------------------------------------------------------------------------|
| `origin`      | `string`            | {origin} path parameter from HTTP request URL.                                                     |
| `destination` | `string`            | {destination} path parameter from HTTP request URL.                                                |
| `route`       | `array` of `string` | Claculated route from `origin` to `destination`. `origin` and `destination` are included in array. |

### HTTP Response `400 Bad Request`

* Route was not found.
* JSON response body is the same as in the `200 OK` response, except:
    * `route` is always an empty array.

HTTP `4xx` and System `5xx` Error
=================================

Actually standard Spring error response structure is used for errors.

One exception `400 BadRequest` with `/routing/{origin}/{destination}` that is used for standard use
case that indicates that rote was not found. Is not an error in HTTP sense.

The reason to use such HTTP status for this use case is that it was specified that way in the
official requirements document.

---

*Future feature* will assume common base response also for those errors.
`hasError` set to `true` will indicate that error occurs and error description will be included in
that common base response structure. 
