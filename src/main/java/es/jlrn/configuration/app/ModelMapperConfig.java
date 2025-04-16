package es.jlrn.configuration.app;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.jlrn.persistence.model.temas.Book;
import es.jlrn.persistence.model.temas.Chapter;
import es.jlrn.persistence.model.temas.Lesson;
import es.jlrn.persistence.model.temas.LevelEntity;
import es.jlrn.persistence.model.temas.Page;
import es.jlrn.persistence.model.temas.Subtopic;
import es.jlrn.persistence.model.temas.Topic;
import es.jlrn.presentation.dto.temas.BookDTO;
import es.jlrn.presentation.dto.temas.ChapterDTO;
import es.jlrn.presentation.dto.temas.LessonDTO;
import es.jlrn.presentation.dto.temas.LevelDTO;
import es.jlrn.presentation.dto.temas.PageDTO;
import es.jlrn.presentation.dto.temas.SubtopicDTO;
import es.jlrn.presentation.dto.temas.TopicDTO;

@Configuration
public class ModelMapperConfig {

        @Bean
        public ModelMapper modelMapper() {
                //
                ModelMapper modelMapper = new ModelMapper();

                // Configuración opcional para una coincidencia más estricta
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

                // Aquí podrías añadir mapeos personalizados si es necesario:
                // modelMapper.createTypeMap(Source.class, Destination.class)
                // .addMapping(Source::getSomeField, Destination::setOtherField);

                // LevelEntity
                // Mapeo: de LevelEntity a LevelDTO, extrae el id de Collection al campo
                // collectionId
                modelMapper.createTypeMap(LevelEntity.class, LevelDTO.class)
                                .addMapping(src -> src.getCollection() != null ? src.getCollection().getId() : null,
                                                LevelDTO::setCollectionId);

                // Mapeo: de LevelDTO a LevelEntity, omite asignar la colección automáticamente
                modelMapper.createTypeMap(LevelDTO.class, LevelEntity.class)
                                .addMappings(mapper -> mapper.skip(LevelEntity::setCollection));

                // Book
                modelMapper.createTypeMap(Book.class, BookDTO.class)
                                .addMapping(src -> src.getLevel().getId(), BookDTO::setLevelId);

                modelMapper.createTypeMap(BookDTO.class, Book.class)
                                .addMappings(map -> map.skip(Book::setLevel));

                // Lesson
                modelMapper.createTypeMap(Lesson.class, LessonDTO.class)
                                .addMapping(src -> src.getBook().getId(), LessonDTO::setBookId);

                modelMapper.createTypeMap(LessonDTO.class, Lesson.class)
                                .addMappings(mapper -> mapper.skip(Lesson::setBook));

                // Chapter
                modelMapper.createTypeMap(Chapter.class, ChapterDTO.class)
                                .addMapping(src -> src.getLesson().getId(), ChapterDTO::setLessonId);

                modelMapper.createTypeMap(ChapterDTO.class, Chapter.class)
                                .addMappings(mapper -> mapper.skip(Chapter::setLesson));

                // Topic
                modelMapper.createTypeMap(Topic.class, TopicDTO.class)
                                .addMapping(src -> src.getChapter().getId(), TopicDTO::setChapterId);

                modelMapper.createTypeMap(TopicDTO.class, Topic.class)
                                .addMappings(mapper -> mapper.skip(Topic::setChapter));

                // SubTopic
                // Subtopic -> SubtopicDTO
                modelMapper.createTypeMap(Subtopic.class, SubtopicDTO.class)
                                .addMapping(src -> src.getTopic().getId(), SubtopicDTO::setTopicId);

                // SubtopicDTO -> Subtopic
                modelMapper.createTypeMap(SubtopicDTO.class, Subtopic.class)
                                .addMappings(mapper -> mapper.skip(Subtopic::setTopic));

                // Page                
                // Page -> PageDTO
                modelMapper.createTypeMap(Page.class, PageDTO.class)
                                .addMapping(src -> src.getSubtopic().getId(), PageDTO::setSubtopicId);

                // PageDTO -> Page
                modelMapper.createTypeMap(PageDTO.class, Page.class)
                                .addMappings(mapper -> mapper.skip(Page::setSubtopic));

                return new ModelMapper();
        }
}
