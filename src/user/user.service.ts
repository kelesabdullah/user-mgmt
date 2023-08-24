import { Injectable, NotFoundException } from '@nestjs/common';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { v4 as uuid } from 'uuid';
import { InjectRepository } from '@nestjs/typeorm';
import { User } from './entities/user.entity';
import { Repository } from 'typeorm';

@Injectable()
export class UserService {
  constructor(
    @InjectRepository(User)
    private userRepository: Repository<User>,
  ) {}

  async create(createUserDto: CreateUserDto) {
    const { name, email } = createUserDto;

    var user = this.userRepository.create({
      id: uuid(),
      name,
      email,
    });
    await this.userRepository.save(user);

    return user;
  }

  async findAll() {
    var users = await this.userRepository.find({});

    return users;
  }

  async findOne(id: string) {
    const user = await this.userRepository.findOneBy({ id });

    return user;
  }

  async update(updateUserDto: UpdateUserDto) {
    const { id, name, email } = updateUserDto;

    var user = await this.userRepository.findOneBy({ id });

    if (name) user.name = name;
    if (email) user.email = email;

    await user.save();

    return user;
  }

  async remove(id: string) {
    const found = await this.userRepository.delete({ id });

    if (!found.affected) {
      throw new NotFoundException();
    }
    return { id };
  }
}
