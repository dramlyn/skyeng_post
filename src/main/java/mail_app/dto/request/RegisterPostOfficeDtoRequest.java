package mail_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


public class RegisterPostOfficeDtoRequest {
    @NotNull
    private int index;
    @NotBlank
    private String name;
    @NotBlank
    private String address;

    public RegisterPostOfficeDtoRequest(int index, String name, String address) {
        this.index = index;
        this.name = name;
        this.address = address;
    }

    public RegisterPostOfficeDtoRequest() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterPostOfficeDtoRequest that = (RegisterPostOfficeDtoRequest) o;
        return getIndex() == that.getIndex() && Objects.equals(getName(), that.getName()) && Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex(), getName(), getAddress());
    }

    @Override
    public String toString() {
        return "RegisterPostOfficeDtoRequest{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
