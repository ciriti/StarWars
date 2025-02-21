# Star Wars App

## Features
- Fetch and display a paginated list of Star Wars characters.
- View detailed profiles of individual characters.
- Offline caching of previously fetched data.

## Use Cases Covered
- **Fetching a paginated list of people**: Retrieves and caches a list of Star Wars characters.
- **Loading additional pages**: Fetches more pages as the user scrolls.
- **Viewing a person's details**: Displays detailed information about a character.
- **Handling errors and displaying messages**: Catches network failures and server errors.
- **Handling empty responses**: Manages cases where the API returns no data.
- **Caching mechanism**: Expires cached data after 2 minutes to balance freshness and efficiency.

### Edge Cases Considered
- **Rapid Pagination Requests**: The app prevents multiple simultaneous requests when fetching new pages.
- **Empty API Responses**: If the API returns an empty list, the app does not attempt further requests.
- **Cache Handling**: Cached data older than 2 minutes is discarded and refreshed from the API.
- **Handling Last Page Scenarios**: If no additional pages exist, the app does not trigger unnecessary API calls.
