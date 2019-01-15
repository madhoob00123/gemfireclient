package com.example.gemfire;

import com.example.gemfire.hello.Person;
import com.example.gemfire.repository.PersonRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;

@SpringBootApplication(scanBasePackages = {"com.example.gemfire.repository"})
//@ClientCacheApplication(name = "AccessingDataGemFireApplication", logLevel = "info", servers = {@ClientCacheApplication.Server(host = "gemfireserver-brash-turtle.cfapps.io", port = 8080)})//locators = {@ClientCacheApplication.Locator(host = "localhost", port = 44444)})
@ClientCacheApplication(name = "AccessingDataGemFireApplication", logLevel = "info", servers = {@ClientCacheApplication.Server(host = "gemfireserver-brash-turtle.cfapps.io")})//locators = {@ClientCacheApplication.Locator(host = "localhost", port = 44444)})
@EnableEntityDefinedRegions(basePackageClasses = Person.class)

@EnableGemfireRepositories
@EnablePdx
public class GemfireApplication {

	public static void main(String[] args) {
		SpringApplication.run(GemfireApplication.class, args);
	}

	@Bean
	ApplicationRunner run(PersonRepository personRepository) {

		return args -> {

			Person alice = new Person("Adult Alice", 40);
			Person bob = new Person("Baby Bob", 1);
			Person carol = new Person("Teen Carol", 13);

			System.out.println("Before accessing data in Pivotal GemFire...");

			asList(alice, bob, carol).forEach(person -> System.out.println("\t" + person));

			System.out.println("Saving Alice, Bob and Carol to Pivotal GemFire...");

			personRepository.save(alice);
			personRepository.save(bob);
			personRepository.save(carol);

			System.out.println("Lookup each person by name...");

			asList(alice.getName(), bob.getName(), carol.getName())
					.forEach(name -> System.out.println("\t" + personRepository.findByName(name)));

			System.out.println("Query adults (over 18):");

			stream(personRepository.findByAgeGreaterThan(18).spliterator(), false)
					.forEach(person -> System.out.println("\t" + person));

			System.out.println("Query babies (less than 5):");

			stream(personRepository.findByAgeLessThan(5).spliterator(), false)
					.forEach(person -> System.out.println("\t" + person));

			System.out.println("Query teens (between 12 and 20):");

			stream(personRepository.findByAgeGreaterThanAndAgeLessThan(12, 20).spliterator(), false)
					.forEach(person -> System.out.println("\t" + person));
		};
	}
}
