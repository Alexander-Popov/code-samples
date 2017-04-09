<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Infrastructure\Exception\CoreException;
use app\Core\Domain\Repository\City\CityReadRepository;

final class CreateCityValidator
{
    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * @param CityReadRepository $cityReadRepository
     */
    public function __construct(CityReadRepository $cityReadRepository)
    {
        $this->cityReadRepository = $cityReadRepository;
    }

    /**
     * @param CreateCityCommand $command
     */
    public function validate(CreateCityCommand $command)
    {
        $this->guardNameIsUniqueInCountry($command->getName(), $command->getCountry());
    }

    /**
     * @param string $name
     * @param string $country
     * @throws CoreException
     */
    private function guardNameIsUniqueInCountry(string $name, string $country)
    {
        if ($this->cityReadRepository->existsByNameAndCountry($name, $country)) {
            throw new CoreException('Такой город уже добавлен для этой страны.');
        }
    }
}