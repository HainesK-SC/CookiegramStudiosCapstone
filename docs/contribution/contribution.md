# Contributions & Collaboration

Not necessary, but let's try to add javadocs/comments to class headers so:
- we know who wrote it
- it acts as a changelog

## Example

**Extracted from Promotion Entity**
- This example shows us what the file's purpose is.
- Who made it
- What date it was created
- Version of the file

```java
/**
 * Promotion entity that represents a promotional offer.
 * <p>
 * Running promotions will be displayed on the main landing page that
 * customers are brought to upon successful login; the promotions on that page
 * will be made up of Promotion entities from this class.
 * </p>
 * <p>
 * The entity includes the following key attributes:
 * </p>
 * <ul>
 * <li><b>id: int</b> - Unique identifier for the promotion</li>
 * <li><b>promoCode: String</b> - A unique promotional code</li>
 * <li><b>description: String</b> - Short description about the promotion</li>
 * <li><b>promoType: String</b> - Type of promotion (fixed dollar amount or set percentage)</li>
 * <li><b>promoValue: String</b> - The actual figure (dollar amount or percentage)</li>
 * <li><b>starDate: LocalDateTime</b> - Date and time the promotion started</li>
 * <li><b>endDate: LocalDateTime</b> - Date and time the promotion ended</li>
 * <li><b>isActive: boolean</b> - Whether the promotion is currently running or not</li>
 * </ul>
 *
 * @author Kyle Haines
 * @date 2026-02-22
 * @version 1.0
 */

public class Promotion {

```


**Versioning should follow this:**
1. If it is a new file added, just add 1.0
2. If you're adding a change (can be code, comments), just add 1.1
3. If it's a major change of the file, just add 2.0
4. And so on

**What does a "major change" mean?**
- If you're changing the name of a class, (refactoring purposes)
- If you're changing the structure of a class (adding or removing fields)
- If you're changing the type of a field (e.g.
- If you're changing the logical flow, and it affects the way the code works in other files