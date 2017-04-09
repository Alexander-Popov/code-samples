<?php
declare(strict_types = 1);

namespace app\Core\Infrastructure\Repository\City;

use app\Core\{
    Domain\Model\City\City,
    Domain\Repository\City\CityRepository,
    Infrastructure\Service\IdentityGenerator\Uuid4Generator
};
use yii\db\Query;

final class YiiSqlCityRepository implements CityRepository
{
    const TABLE_NAME = 'city';

    /**
     * @var CityHydrator
     */
    private $cityHydrator;

    /**
     * YiiSqlCityReadRepository constructor.
     */
    public function __construct()
    {
        $this->cityHydrator = new CityHydrator();
    }

    /**
     * @param City $city
     */
    public function add(City $city)
    {
        $columns = $this->cityHydrator->extract($city);
        $columns['id'] = $city->getId();
        $query = new Query();
        $query->createCommand()->insert(self::TABLE_NAME, $columns)->execute();
    }

    /**
     * @param City $city
     */
    public function save(City $city)
    {
        $columns = $this->cityHydrator->extract($city);
        $query = new Query();
        $query->createCommand()->update(self::TABLE_NAME, $columns, ['id' => $city->getId()])->execute();
    }

    /**
     * @param City $city
     */
    public function remove(City $city)
    {
        $query = new Query();
        $query->createCommand()->delete(self::TABLE_NAME, ['id' => $city->getId()])->execute();
    }

    /**
     * Get next id
     *
     * @return string
     */
    public function nextIdentity() : string
    {
        $uuidGenerator = new Uuid4Generator();
        return $uuidGenerator->generate();
    }
}