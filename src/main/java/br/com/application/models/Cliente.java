package br.com.application.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Cliente implements Serializable {
	private static final long serialVersionUID = 5135465126329174969L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;

	@Column(unique = true)
	private String cpf;

	private String dataNascimento;

	@Nullable
	@Transient
	private Integer idade;

}
