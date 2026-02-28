package com.cookiegramstudios.cookiegram.customization;
import com.cookiegramstudios.cookiegram.cookie.Cookie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cookie_customizations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CookieCustomization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cookie_id", nullable = false)
    private Cookie cookie;

    private String icingType;
    private String toppings;
    private String messageText;
    private double additionalCost;

    // Future-proofing for dietary info
    private String specialDietaryInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cookie getCookie() {
		return cookie;
	}

	public void setCookie(Cookie cookie) {
		this.cookie = cookie;
	}

	public String getIcingType() {
		return icingType;
	}

	public void setIcingType(String icingType) {
		this.icingType = icingType;
	}

	public String getToppings() {
		return toppings;
	}

	public void setToppings(String toppings) {
		this.toppings = toppings;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public double getAdditionalCost() {
		return additionalCost;
	}

	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}

	public String getSpecialDietaryInfo() {
		return specialDietaryInfo;
	}

	public void setSpecialDietaryInfo(String specialDietaryInfo) {
		this.specialDietaryInfo = specialDietaryInfo;
	}
    
    
}
