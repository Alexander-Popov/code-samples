<?php
declare(strict_types = 1);

namespace app\Core\Application\Command\City;

use app\Core\Domain\Repository\City\CityReadRepository;
use app\Core\Domain\Repository\City\CityRepository;

final class DeleteCityHandler
{
    /**
     * @var CityRepository
     */
    private $cityRepository;

    /**
     * @var CityReadRepository
     */
    private $cityReadRepository;

    /**
     * DeleteCityHandler constructor.
     * @param CityRepository $cityRepository
     * @param CityReadRepository $cityReadRepository
     */
    public function __construct(
        CityRepository $cityRepository,
        CityReadRepository $cityReadRepository
    )
    {
        $this->cityRepository = $cityRepository;
        $this->cityReadRepository = $cityReadRepository;
    }

    /**
     * @param DeleteCityCommand $command
     */
    public function handle(DeleteCityCommand $command)
    {
        $city = $this->cityReadRepository->findById($command->getId());
        $this->cityRepository->remove($city);
    }
}