package uz.mediasolutions.miticodeliverytelegrambot.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.miticodeliverytelegrambot.entity.template.AbsLong;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "files")
public class FileImage extends AbsLong {

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

}
